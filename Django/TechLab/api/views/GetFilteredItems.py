from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404

from api.models import (Book, Electronic)

import json


# Create your views here.
class GetFilteredItems(View):
    def get(self, request, *args, **kwargs):
        name = (request.GET.get('title', '') if  request.GET.get('title', '') != '' else request.GET.get('name', '')
        if request.GET.get('name', '') != '' else '')

        allBooks = Book.objects.filter(title=name)
        allElectronics = Electronic.objects.filter(name=name)

        return JsonResponse(json.loads('[{ "books": %s}, {"electronics" : %s}]' % (
            json.dumps([book.to_json('_state', 'item_ptr_id', 'writers') for book in allBooks]),
            json.dumps([electronic.to_json('_state', 'item_ptr_id') for electronic in allElectronics]))), safe=False)


class GetFilteredBooks(View):
    def get(self, request, pk, *args, **kwargs):
        print(pk)

        book = get_object_or_404(Book, id=pk)

        return JsonResponse(json.loads(json.dumps(book.to_json('_state', 'item_ptr_id'))), safe=False)


class GetFilteredElectronics(View):
    def get(self, request, pk, *args, **kwargs):
        print(pk)
        electronic = get_object_or_404(Electronic, id=pk)

        return JsonResponse(json.loads(json.dumps(electronic.to_json('_state', 'item_ptr_id'))), safe=False)
