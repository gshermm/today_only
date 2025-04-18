# Generated by Django 4.2.16 on 2024-12-06 05:39

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0004_deal_latitude_deal_longitude'),
    ]

    operations = [
        migrations.AddField(
            model_name='deal',
            name='recurrence_days',
            field=models.JSONField(blank=True, default=list, null=True),
        ),
        migrations.AlterField(
            model_name='deal',
            name='rating',
            field=models.FloatField(default=5.0),
        ),
    ]
