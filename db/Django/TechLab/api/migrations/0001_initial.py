# Generated by Django 2.1.7 on 2019-03-19 12:48

from django.db import migrations, models
import django.db.models.deletion
import uuid


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='BorrowItem',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('borrow_date', models.DateTimeField()),
                ('return_date', models.DateTimeField()),
                ('hand_in_date', models.DateTimeField()),
            ],
        ),
        migrations.CreateModel(
            name='Item',
            fields=[
                ('id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False, unique=True)),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False, unique=True)),
                ('google_api_id', models.CharField(max_length=128)),
                ('email', models.EmailField(max_length=254)),
            ],
        ),
        migrations.CreateModel(
            name='Electronic',
            fields=[
                ('item_ptr', models.OneToOneField(auto_created=True, on_delete=django.db.models.deletion.CASCADE, parent_link=True, primary_key=True, serialize=False, to='api.Item')),
                ('product_id', models.CharField(max_length=64)),
                ('manufacturer', models.CharField(max_length=128)),
                ('category', models.CharField(max_length=128)),
                ('name', models.CharField(max_length=128)),
                ('stock', models.IntegerField()),
                ('broken', models.IntegerField()),
            ],
            bases=('api.item',),
        ),
        migrations.CreateModel(
            name='Book',
            fields=[
                ('item_ptr', models.OneToOneField(auto_created=True, on_delete=django.db.models.deletion.CASCADE, parent_link=True, primary_key=True, serialize=False, to='api.Item')),
                ('title', models.CharField(max_length=128)),
                ('writers', models.CharField(max_length=128)),
                ('isbn', models.CharField(max_length=128)),
                ('publisher', models.CharField(max_length=128)),
                ('stock', models.IntegerField()),
            ],
            bases=('api.item',),
        ),
        migrations.AddField(
            model_name='borrowitem',
            name='item',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='borrow_item_item', to='api.Item'),
        ),
        migrations.AddField(
            model_name='borrowitem',
            name='user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='borrow_item_item', to='api.User'),
        ),
    ]
