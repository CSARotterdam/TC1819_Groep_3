from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (BorrowItem)

import json


class BorrowItems(View):
    def get(self, request, *atgs, **kwargs):
        email = request.GET.get('email', '*')
        allBorrowedItems = BorrowItem.objects.filter(hand_in_date=None)

        return JsonResponse(json.loads(
            json.dumps([borrowItem.to_json('_state', 'item_ptr_id', 'writers') for borrowItem in allBorrowedItems]),
        ), safe=False)

    def put(self, request, pk, *args, **kwargs):
        borrowItem = get_object_or_404(BorrowItem, id=pk)

        borrowItem.hand_in_date = datetime.datetime.now()
        borrowItem.save()

        return JsonResponse(json.loads('{"success": "true"}'), safe=False)


class ReturnItems(View):
    def get(self, request, *atgs, **kwargs):
        email = request.GET.get('email', '*')
        allBorrowedItems = BorrowItem.objects.exclude(hand_in_date=None)

        return JsonResponse(json.loads(
            json.dumps([borrowItem.to_json('_state', 'item_ptr_id', 'writers') for borrowItem in allBorrowedItems]),
        ), safe=False)

