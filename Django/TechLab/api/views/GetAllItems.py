from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.db.models import Prefetch

from api.models import (Book, Electronic, Writer)

import json


# Create your views here.
class GetAllItems(View):
    def get(self, request, *args, **kwargs):

        allBooks = Book.objects.all()
        allElectronics = Electronic.objects.all()

        return JsonResponse(json.loads('[{ "books": %s}, {"electronics" : %s}]' % (
            json.dumps([book.to_json('_state', 'item_ptr_id', 'writers') for book in allBooks]),
            json.dumps([electronic.to_json('_state', 'item_ptr_id') for electronic in allElectronics]))), safe=False)

class GetAllBooks(View):
    def get(self, request, *args, **kwargs):
        allBooks = Book.objects.all()
        return JsonResponse(json.loads(json.dumps([book.to_json('_state', 'item_ptr_id') for book in allBooks])), safe=False)

class GetAllElectronics(View):
    def get(self, request, *args, **kwargs):
        allElectronics = Electronic.objects.all()
        return JsonResponse(json.loads(json.dumps([electronic.to_json('_state', 'item_ptr_id') for electronic in allElectronics])), safe=False)
