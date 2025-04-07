# deals/serializers.py

from rest_framework import serializers
from .models import Deal, Rating


class DealListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Deal
        fields = [
            'id',
            'restaurant_name',
            'title',
            'description',
            'start_time',
            'end_time',
            'date',
            'student_id_required',
            'rating',
            'total_ratings',
        ]


class DealDetailSerializer(serializers.ModelSerializer):
    class Meta:
        model = Deal
        fields = [
            'id',
            'restaurant_name',
            'title',
            'description',
            'start_time',
            'end_time',
            'date',
            'student_id_required',
            'rating',
            'total_ratings',
            'requirements',
            'location',
        ]


class DealSubmitSerializer(serializers.ModelSerializer):
    recurrence_days = serializers.ListField(
        child=serializers.CharField(),
        required=False,
        allow_empty=True,
        help_text="List of weekdays when the deal recurs (e.g. ['monday', 'friday'])"
    )

    class Meta:
        model = Deal
        fields = [
            'id',
            'restaurant_name',
            'title',
            'description',
            'date',
            'start_time',
            'end_time',
            'student_id_required',
            'requirements',
            'contact_email',
            'recurrence_days',  # add this field
        ]
        read_only_fields = ['id', 'status']


class RatingSerializer(serializers.ModelSerializer):
    class Meta:
        model = Rating
        fields = ['rating']
