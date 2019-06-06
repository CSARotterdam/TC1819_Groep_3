from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.db.models import Prefetch
from django.contrib.auth.models import User as AuthUser
from django.shortcuts import get_object_or_404
from django.db.models import prefetch_related_objects

from api.models import (Book, Electronic, Writer, Category, Publisher, Item, User)

import json


# Create your views here.
class GetAllItems(View):
    def get(self, request, *args, **kwargs):
        allBooks = Book.objects.all()
        allElectronics = Electronic.objects.all()

        return JsonResponse(json.loads('%s' % (
            json.dumps([book.to_json('item_ptr', 'borrow_item_item') for book in allBooks] +
                       [electronic.to_json('item_ptr', 'borrow_item_item') for electronic in allElectronics]))),
                            safe=False, content_type='application/json')

class GetAllBooks(View):
    def get(self, request, *args, **kwargs):
        allBooks = Book.objects.all()
        return JsonResponse(
            json.loads(json.dumps([book.to_json('_state', 'item_ptr', 'borrow_item_item') for book in allBooks])),
            safe=False, content_type='application/json')

    def post(self, request, *args, **kwargs):
        if 'username' in request.POST:
            admin = AuthUser.objects.filter(username=request.POST.get('username')).first() if \
                AuthUser.objects.filter(username=request.POST.get('username')).count() > 0 else None

            if admin is not None:
                book = Book.objects.create(borrow_days=int(request.POST.get('borrow_days')),
                                           description=request.POST.get('description'),
                                           title=request.POST.get('title'),
                                           isbn=request.POST.get('isbn'),
                                           publisher=Publisher.objects.get(id=request.POST.get('publisher')),
                                           stock=int(request.POST.get('stock')), )
                if 'image' in request.FILES:
                    book.image = request.FILES['image']
                    pass
                book.save()

                return JsonResponse('{"success": "true", "message": "The item has been created."}',
                                    safe=False, status=200, content_type='application/json')

        return JsonResponse('', safe=False, status=401)

class GetAllElectronics(View):
    def get(self, request, *args, **kwargs):
        allElectronics = Electronic.objects.all()
        return JsonResponse(json.loads(json.dumps([electronic.to_json('_state', 'item_ptr', 'borrow_item_item') for
                                                   electronic in allElectronics])), safe=False,
                            content_type='application/json')

    def post(self, request, *args, **kwargs):
        if 'username' in request.POST:
            admin = AuthUser.objects.filter(username=request.POST['username']).first() if \
                AuthUser.objects.filter(username=request.POST['username']).count() > 0 else None

            if admin is not None:
                electronic = Electronic.objects.create(borrow_days=request.POST['borrow_days'],
                                                       description=request.POST['description'],
                                                       product_id=request.POST['product_id'],
                                                       name=request.POST['name'],
                                                       category=Category.objects.get(id=request.POST['category']),
                                                       stock=request.POST['stock'],
                                                       broken=request.POST['broken'])
                electronic.save()

                return JsonResponse('{"success": "true", "message": "The item has been created."}',
                                    safe=False, status=200, content_type='application/json')

        return JsonResponse('', safe=False, status=401, content_type='application/json')


class GetItem(View):
    def get(self, request, pk):
        item = get_object_or_404(Item, id=pk)
        model_item = get_object_or_404(eval(item.type), id=pk)

        return JsonResponse(json.loads(json.dumps(model_item.to_json('_state', 'item_ptr'))), safe=False, content_type='application/json')
