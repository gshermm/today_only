# Generated by Django 4.2.16 on 2024-11-22 02:09

from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Restaurant',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=255, unique=True)),
                ('location', models.CharField(max_length=255)),
            ],
        ),
        migrations.CreateModel(
            name='Deal',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=100)),
                ('description', models.CharField(max_length=500)),
                ('date', models.DateField(default=django.utils.timezone.now)),
                ('start_time', models.TimeField()),
                ('end_time', models.TimeField()),
                ('student_id_required', models.BooleanField(default=False)),
                ('requirements', models.CharField(blank=True, max_length=200, null=True)),
                ('contact_email', models.EmailField(max_length=254)),
                ('status', models.CharField(choices=[('active', 'Active'), ('upcoming', 'Upcoming'), ('pending', 'Pending Review')], default='pending', max_length=20)),
                ('rating', models.FloatField(default=0.0)),
                ('total_ratings', models.PositiveIntegerField(default=0)),
                ('restaurant', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='deals', to='deals.restaurant')),
            ],
        ),
    ]
