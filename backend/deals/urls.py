# deals/urls.py

from django.urls import path
from .views import DealListCreateView, DealDetailView, DealRateView

urlpatterns = [
    path('deals/', DealListCreateView.as_view(), name='deal-list-create'),
    path('deals/<uuid:id>/', DealDetailView.as_view(), name='deal-detail'),
    path('deals/<uuid:deal_id>/rate/', DealRateView.as_view(), name='deal-rate'),
]
