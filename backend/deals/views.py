# deals/views.py

from rest_framework import generics, status
from rest_framework.response import Response
from rest_framework.exceptions import ValidationError, NotFound
from django.utils import timezone
from django.db.models import Avg, Q, F
from django.db.models.functions import Sqrt, Power
from datetime import datetime
import calendar

from .models import Deal, Rating
from .serializers import (
    DealListSerializer,
    DealDetailSerializer,
    DealSubmitSerializer,
    RatingSerializer,
)


class DealListCreateView(generics.GenericAPIView):
    queryset = Deal.objects.all()

    def get_serializer_class(self):
        if self.request.method == 'POST':
            return DealSubmitSerializer
        return DealListSerializer

    def get(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        serializer = DealListSerializer(queryset, many=True)
        return Response({'deals': serializer.data})

    def get_queryset(self):
        queryset = Deal.objects.all()

        sort_by = self.request.query_params.get('sort_by', 'rating')
        date = self.request.query_params.get('date', timezone.now().date().isoformat())
        view_type = self.request.query_params.get('view_type', 'day')
        status_param = self.request.query_params.get('status', 'active')
        latitude = self.request.query_params.get('latitude', None)
        longitude = self.request.query_params.get('longitude', None)

        # Filter by status
        if status_param in ['active', 'upcoming']:
            queryset = queryset.filter(status=status_param)

        # Parse date
        try:
            date_obj = datetime.strptime(date, '%Y-%m-%d').date()
        except ValueError:
            raise ValidationError({
                'error': 'validation_error',
                'message': 'Invalid date format. Use YYYY-MM-DD'
            })

        if view_type == 'day':
            date_range = [date_obj]
        elif view_type == 'week':
            week_start = date_obj - timezone.timedelta(days=date_obj.weekday())
            date_range = [week_start + timezone.timedelta(days=i) for i in range(7)]
        elif view_type == 'month':
            _, last_day = calendar.monthrange(date_obj.year, date_obj.month)
            date_range = [date_obj.replace(day=d) for d in range(1, last_day + 1)]
        else:
            date_range = [date_obj]

        # Determine weekdays in range
        weekdays_in_range = list({calendar.day_name[d.weekday()].lower() for d in date_range})

        filters = Q()
        for w in weekdays_in_range:
            filters |= Q(recurrence_days__icontains=f'{w}')
        queryset = queryset.filter(filters | Q(date__in=date_range))

        # Sorting
        if sort_by == 'rating':
            queryset = queryset.order_by('-rating')
        elif sort_by == 'distance':
            if latitude is None or longitude is None:
                raise ValidationError({
                    'error': 'validation_error',
                    'message': 'latitude and longitude are required when sort_by=distance'
                })
            user_latitude = float(latitude)
            user_longitude = float(longitude)
            queryset = queryset.annotate(
                distance=Sqrt(
                    Power(F('latitude') - user_latitude, 2) +
                    Power(F('longitude') - user_longitude, 2)
                )
            ).order_by('distance')
        else:
            queryset = queryset.order_by('-rating')

        return queryset

    def post(self, request, *args, **kwargs):
        serializer = DealSubmitSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        deal = serializer.save()
        return Response({
            'id': str(deal.id),
            'status': deal.status,
            'message': 'Deal submitted successfully and pending review'
        }, status=status.HTTP_201_CREATED)


class DealDetailView(generics.RetrieveAPIView):
    queryset = Deal.objects.all()
    serializer_class = DealDetailSerializer
    lookup_field = 'id'

    def get(self, request, *args, **kwargs):
        try:
            deal = self.get_object()
        except Deal.DoesNotExist:
            raise NotFound({
                'error': 'not_found',
                'message': 'Deal not found'
            })
        serializer = self.get_serializer(deal)
        return Response(serializer.data)


class DealRateView(generics.CreateAPIView):
    serializer_class = RatingSerializer

    def post(self, request, *args, **kwargs):
        deal_id = self.kwargs.get('deal_id')
        try:
            deal = Deal.objects.get(id=deal_id)
        except Deal.DoesNotExist:
            raise NotFound({
                'error': 'not_found',
                'message': 'Deal not found'
            })

        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        rating_value = serializer.validated_data['rating']

        if not 1 <= rating_value <= 5:
            raise ValidationError({
                'error': 'validation_error',
                'message': 'Rating value must be between 1 and 5'
            })

        Rating.objects.create(deal=deal, rating=rating_value)

        total_ratings = Rating.objects.filter(deal=deal).count()
        avg_rating = Rating.objects.filter(deal=deal).aggregate(Avg('rating'))['rating__avg']
        deal.total_ratings = total_ratings
        deal.rating = round(avg_rating, 2)
        deal.save()

        return Response({
            'success': True,
            'new_average_rating': deal.rating,
            'total_ratings': total_ratings
        }, status=status.HTTP_200_OK)
