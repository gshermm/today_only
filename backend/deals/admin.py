# deals/admin.py
from django.contrib import admin
from .models import Deal


@admin.register(Deal)
class DealAdmin(admin.ModelAdmin):
    list_display = ('restaurant_name', 'title', 'date', 'status', 'rating')
    list_filter = ('status', 'date', 'student_id_required')
    search_fields = ('restaurant_name', 'title', 'description')
    # readonly_fields = ('rating', 'total_ratings')
