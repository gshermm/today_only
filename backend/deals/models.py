# deals/models.py

import uuid
from django.db import models
from django.db.models import JSONField


class Deal(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    restaurant_name = models.CharField(max_length=255)
    title = models.CharField(max_length=100)
    description = models.TextField(max_length=500)
    date = models.DateField(blank=True, null=True)
    start_time = models.TimeField()
    end_time = models.TimeField()
    student_id_required = models.BooleanField(default=False)
    requirements = models.CharField(max_length=200, blank=True, null=True)
    location = models.CharField(max_length=255, blank=True, null=True)
    longitude = models.FloatField(blank=True, null=True)
    latitude = models.FloatField(blank=True, null=True)
    contact_email = models.EmailField(blank=True, null=True)
    total_ratings = models.PositiveIntegerField(default=0)
    rating = models.FloatField(default=5.0)  # Average rating
    recurrence_days = JSONField(blank=True, null=True, default=list)

    STATUS_CHOICES = [
        ('pending', 'Pending'),
        ('active', 'Active'),
        ('upcoming', 'Upcoming'),
    ]
    status = models.CharField(max_length=10, choices=STATUS_CHOICES, default='pending')

    def __str__(self):
        return f"{self.title} at {self.restaurant_name}"


class Rating(models.Model):
    deal = models.ForeignKey(Deal, on_delete=models.CASCADE, related_name='ratings')
    rating = models.PositiveSmallIntegerField()  # Rating from 1 to 5
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Rating {self.rating} for {self.deal.title}"
