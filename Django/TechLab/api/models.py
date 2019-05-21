from django.db import models
from datetime import datetime
from django.conf import settings
import uuid
from django.contrib.auth.models import User as AuthUser

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
        print(vars(self))
        json_dict = vars(self)
        json_dict['id'] = str(json_dict['id'])

        for arg in exclude_vars:
            json_dict.pop(arg, None)
        print(json_dict)
        return json_dict


class API(models.Model):
    user = models.ForeignKey(AuthUser, on_delete=models.CASCADE)
    api_key = models.UUIDField(default=uuid.uuid4())


class Item(models.Model):
    id = models.UUIDField(default=uuid.uuid4, editable=False, unique=True, primary_key=True)
    borrow_days = models.IntegerField(default=0)
    description = models.CharField(max_length=2048, default="")
    image = models.ImageField(blank=True, upload_to='static')

    def to_json(self, *exclude_vars):
        print(vars(self))
        json_dict = vars(self)
        json_dict['id'] = str(json_dict['id'])

        for arg in exclude_vars:
            json_dict.pop(arg, None)
        print(json_dict)
        return json_dict


class Writer(models.Model):
    def __str__(self):
        return self.name
    name = models.CharField(max_length=128, default="")


class Category(models.Model):
    def __str__(self):
        return self.name
    name = models.CharField(max_length=128, default="")


class Manufacturer(models.Model):
    def __str__(self):
        return self.name
    name = models.CharField(max_length=128, default="")


class Publisher(models.Model):
    def __str__(self):
        return self.name
    name = models.CharField(max_length=128, default="")


class Book(Item):
    title = models.CharField(max_length=128)
    writers = models.ManyToManyField(Writer, blank=True)
    isbn = models.CharField(max_length=128, default = "")
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
        print(vars(self))
        json_dict = vars(self)
        json_dict['item_id'] = str(json_dict['item_id'])
        json_dict['item'] = Item.objects.get(id=json_dict['item_id']).to_json('_state', 'item_ptr_id')
        json_dict['user_id'] = str(json_dict['user_id'])
        json_dict['borrow_date'] = str(json_dict['borrow_date'])
        json_dict['return_date'] = str(json_dict['return_date'])
        json_dict['hand_in_date'] = str(json_dict['hand_in_date'])

        for arg in exclude_vars:
            json_dict.pop(arg, None)
        print(json_dict)
        return json_dict
