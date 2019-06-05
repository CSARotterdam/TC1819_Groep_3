from django.db import models
from datetime import datetime
from django.conf import settings
import uuid
from django.contrib.auth.models import User as AuthUser
from django.db.models.signals import post_save
from django.dispatch import receiver

# TODO: For each model make a to_json function that takes all of the model's variables and
#  puts them in a json dump, variables can dynamically be excluded, by adding the names into
#  the param of the function(as a list of names.)


# Create your models here.
class User(models.Model):
    id = models.UUIDField(default=uuid.uuid4, editable=False, unique=True, primary_key=True)
    email = models.EmailField()
    is_manager = models.BooleanField(default=False)

    def __str__(self):
        return self.email

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state',)
        for field in self._meta.fields:
            try:
                if field.name not in exclude_vars:
                    if str(type(getattr(self, field.name))).find('api') != -1:
                        item.update({field.name: getattr(self, field.name).to_json()})
                    else:
                        item.update({field.name: str(getattr(self, field.name))})
            except:
                pass

        return item


class API(models.Model):
    user = models.ForeignKey(AuthUser, on_delete=models.CASCADE)
    api_key = models.UUIDField(default=uuid.uuid4())


class Item(models.Model):
    type = models.CharField(max_length=256, null=True, blank=True, editable=False)
    id = models.UUIDField(default=uuid.uuid4, editable=False, unique=True, primary_key=True)
    borrow_days = models.IntegerField(default=0)
    description = models.CharField(max_length=2048, default="")
    image = models.ImageField(blank=True, upload_to='TechLab/static') # TODO: Return a valid abs url

    def save(self, *args, **kwargs):
        self.type = type(self).__name__
        super().save(*args, **kwargs)  # Call the "real" save() method.

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state', )
        for field in self._meta.fields:
            try:
                if field.name not in exclude_vars:
                    if str(type(getattr(self, field.name))).find('api') != -1:
                        item.update({field.name: getattr(self, field.name).to_json()})
                    elif str(type(getattr(self, field.name))).find("ImageFieldFile") != -1:
                        url = None if str(getattr(self, field.name)) == "" else \
                            (settings.BASE_URL + getattr(self, field.name).name.replace('TechLab/static', 'static'))
                        item.update({field.name: url})
                    else:
                        item.update({field.name: str(getattr(self, field.name))})
            except:
                pass

        for field in [x for x in self._meta.get_fields() if x not in self._meta.fields and x.name not in exclude_vars]:
            print(field.name)
            # if field.name not in exclude_vars:
            try:
                item.update({field.name: [x.to_json('borrow_item_item') for x in getattr(self, field.name).all()]})
            except:
                pass
        return item


class Writer(models.Model):
    name = models.CharField(max_length=128, default="")

    def __str__(self):
        return self.name

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state',)
        for field in self._meta.fields:
            try:
                if field.name not in exclude_vars:
                    if str(type(getattr(self, field.name))).find('api') != -1:
                        item.update({field.name: getattr(self, field.name).to_json()})
                    else:
                        item.update({field.name: str(getattr(self, field.name))})
            except:
                pass
        return item


class Category(models.Model):
    name = models.CharField(max_length=128, default="")

    def __str__(self):
        return self.name

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state',)
        for field in self._meta.fields:
            try:
                if field.name not in exclude_vars:
                    if str(type(getattr(self, field.name))).find('api') != -1:
                        item.update({field.name: getattr(self, field.name).to_json()})
                    else:
                        item.update({field.name: str(getattr(self, field.name))})
            except:
                pass
        return item


class Manufacturer(models.Model):
    name = models.CharField(max_length=128, default="")

    def __str__(self):
        return self.name

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state',)
        for field in self._meta.fields:
            try:
                if field.name not in exclude_vars:
                    if str(type(getattr(self, field.name))).find('api') != -1:
                        item.update({field.name: getattr(self, field.name).to_json()})
                    else:
                        item.update({field.name: str(getattr(self, field.name))})
            except:
                pass
        return item


class Publisher(models.Model):
    name = models.CharField(max_length=128, default="")

    def __str__(self):
        return self.name

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state',)
        for field in self._meta.fields:
            try:
                if field.name not in exclude_vars:
                    if str(type(getattr(self, field.name))).find('api') != -1:
                        item.update({field.name: getattr(self, field.name).to_json()})
                    else:
                        item.update({field.name: str(getattr(self, field.name))})
            except:
                pass

        return item


class Book(Item):
    title = models.CharField(max_length=128)
    writers = models.ManyToManyField(Writer, blank=True)
    isbn = models.CharField(max_length=128)
    publisher = models.ForeignKey(Publisher, on_delete=models.CASCADE)
    stock = models.IntegerField(default=0)

    def __str__(self):
        return self.title


class Electronic(Item):
    product_id = models.CharField(max_length=64, default="")
    manufacturer = models.ManyToManyField(Manufacturer, blank=True)
    category = models.ForeignKey(Category, on_delete=models.CASCADE)
    name = models.CharField(max_length=128, default="")
    stock = models.IntegerField(default=0)
    broken = models.IntegerField(default=0)

    def __str__(self):
        return self.name


class BorrowItem(models.Model):
    item = models.ForeignKey(Item, related_name="borrow_item_item", on_delete=models.CASCADE)
    user = models.ForeignKey(User, related_name="borrow_item_item", on_delete=models.CASCADE)
    borrow_date = models.DateTimeField(default=datetime.now())
    return_date = models.DateTimeField(default=datetime.now())
    hand_in_date = models.DateTimeField(blank=True, null=True)

    def __str__(self):
        return str(self.user) + '( ' + str(self.borrow_date) + ' - ' + str(self.return_date) + ' )'

    def to_json(self, *exclude_vars):
        item = {}
        exclude_vars = exclude_vars + ('_state',)

        for field in self._meta.fields:
            # try:
            if field.name not in exclude_vars:
                print("field", field.name)
                if str(type(getattr(self, field.name))).find('api') != -1:
                    item.update({field.name: getattr(self, field.name).to_json('borrow_item_item')})
                else:
                    item.update({field.name: str(getattr(self, field.name))})
            # except:
            #     pass

        for field in [x for x in self._meta.get_fields() if x not in self._meta.fields and x.name not in exclude_vars]:
            # if field.name not in exclude_vars:
            item.update({field.name: str(getattr(self, field.name))})

        return item
