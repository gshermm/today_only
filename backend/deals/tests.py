# # deals/tests.py

# from django.test import TestCase
# from django.urls import reverse
# from rest_framework import status
# from rest_framework.test import APIClient
# from .models import Deal
# import uuid


# class DealAPITests(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.deal = Deal.objects.create(
#             restaurant_name="Test Restaurant",
#             title="Test Deal",
#             description="Test Description",
#             date="2024-01-20",
#             start_time="11:00",
#             end_time="14:00",
#             student_id_required=True,
#             contact_email="contact@example.com",
#             status="active",
#         )
#         self.deal_id = str(self.deal.id)

#     def test_list_deals(self):
#         response = self.client.get('/api/deals/')
#         self.assertEqual(response.status_code, status.HTTP_200_OK)

#     def test_get_deal_detail(self):
#         response = self.client.get(f'/api/deals/{self.deal_id}/')
#         self.assertEqual(response.status_code, status.HTTP_200_OK)

#     def test_create_deal(self):
#         data = {
#             "restaurant_name": "New Restaurant",
#             "title": "New Deal",
#             "description": "New Description",
#             "date": "2024-02-20",
#             "start_time": "12:00",
#             "end_time": "15:00",
#             "student_id_required": False,
#             "contact_email": "new_contact@example.com"
#         }
#         response = self.client.post('/api/deals/', data, format='json')
#         self.assertEqual(response.status_code, status.HTTP_201_CREATED)

#     def test_rate_deal(self):
#         data = {"rating": 5}
#         response = self.client.post(f'/api/deals/{self.deal_id}/rate/', data, format='json')
#         self.assertEqual(response.status_code, status.HTTP_200_OK)

# deals/tests.py

from django.test import TestCase
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient
from .models import Deal
import uuid
from datetime import datetime


class DealAPITests(TestCase):
    def setUp(self):
        self.client = APIClient()

        # A deal with a specific date
        self.deal_with_date = Deal.objects.create(
            restaurant_name="Test Restaurant",
            title="One-time Deal",
            description="Valid only on this specific day.",
            date="2024-12-10",  # Specific date
            start_time="11:00",
            end_time="14:00",
            student_id_required=False,
            contact_email="contact@example.com",
            status="active",
        )

        # A recurring deal (Monday and Wednesday)
        self.recurring_deal = Deal.objects.create(
            restaurant_name="Recurring Café",
            title="Weekly Coffee Special",
            description="Special deal every Monday and Wednesday.",
            date=None,  # No specific date
            start_time="08:00",
            end_time="10:00",
            student_id_required=False,
            contact_email="deals@recurringcafe.com",
            status="active",
            recurrence_days=["monday", "wednesday"],  # Recurs on Monday and Wednesday
        )

        # A recurring deal without today in recurrence_days
        self.non_matching_recurring_deal = Deal.objects.create(
            restaurant_name="Non-matching Deal",
            title="Not Today Special",
            description="Doesn't match today's weekday.",
            date=None,  # No specific date
            start_time="09:00",
            end_time="12:00",
            student_id_required=False,
            contact_email="deals@notoday.com",
            status="active",
            recurrence_days=["friday"],  # Recurs only on Friday
        )

    def test_list_deals_on_specific_date(self):
        """Test that deals are correctly filtered for a specific date"""
        response = self.client.get('/api/deals/', {'date': '2024-12-10'})
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        deals = response.json().get('deals', [])
        deal_ids = [deal['id'] for deal in deals]

        # Assert only the deal with a matching specific date is returned
        self.assertIn(str(self.deal_with_date.id), deal_ids)
        self.assertNotIn(str(self.recurring_deal.id), deal_ids)
        self.assertNotIn(str(self.non_matching_recurring_deal.id), deal_ids)

    def test_list_deals_on_recurrence_day(self):
        """Test that recurring deals are returned based on recurrence_days"""
        # Simulate a Monday query
        response = self.client.get('/api/deals/', {'date': '2024-12-09'})  # 2024-12-09 is a Monday
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        deals = response.json().get('deals', [])
        deal_ids = [deal['id'] for deal in deals]

        # Assert the recurring deal with Monday in recurrence_days is returned
        self.assertIn(str(self.recurring_deal.id), deal_ids)
        self.assertNotIn(str(self.non_matching_recurring_deal.id), deal_ids)

    def test_list_deals_on_null_date_and_recurrence(self):
        """Test that deals with NULL date and matching recurrence_days are returned"""
        # Simulate a Wednesday query
        response = self.client.get('/api/deals/', {'date': '2024-12-11'})  # 2024-12-11 is a Wednesday
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        deals = response.json().get('deals', [])
        deal_ids = [deal['id'] for deal in deals]

        # Assert the recurring deal with Wednesday in recurrence_days is returned
        self.assertIn(str(self.recurring_deal.id), deal_ids)

    def test_list_deals_excludes_non_matching_recurrence(self):
        """Test that deals with recurrence_days not matching today's weekday are excluded"""
        # Simulate a Wednesday query
        response = self.client.get('/api/deals/', {'date': '2024-12-11'})  # 2024-12-11 is a Wednesday
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        deals = response.json().get('deals', [])
        deal_ids = [deal['id'] for deal in deals]

        # Assert the non-matching recurring deal is not returned
        self.assertNotIn(str(self.non_matching_recurring_deal.id), deal_ids)

    def test_list_deals_without_date(self):
        """Test deals are filtered correctly when no date is specified (use today's date)"""
        # Simulate today's date (use datetime.now())
        today = datetime.now().strftime('%Y-%m-%d')
        weekday_name = datetime.now().strftime('%A').lower()  # Today's weekday name (e.g., "monday")

        response = self.client.get('/api/deals/')  # No date provided
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        deals = response.json().get('deals', [])
        deal_ids = [deal['id'] for deal in deals]

        # Assert deals with matching recurrence_days for today are returned
        if weekday_name in self.recurring_deal.recurrence_days:
            self.assertIn(str(self.recurring_deal.id), deal_ids)
        else:
            self.assertNotIn(str(self.recurring_deal.id), deal_ids)
