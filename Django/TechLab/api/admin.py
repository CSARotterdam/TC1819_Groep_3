from django.contrib import admin

from .models import (User, Book, BorrowItem, Electronic, Writer, Category, Manufacturer, Publisher, API)

# Register your models here.


admin.site.register(User)
admin.site.register(Book)
admin.site.register(BorrowItem)
admin.site.register(Electronic)
admin.site.register(Writer)
admin.site.register(Category)
admin.site.register(Manufacturer)
admin.site.register(Publisher)
admin.site.register(API)
