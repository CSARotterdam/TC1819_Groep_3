from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (User, BorrowItem, Item)
import json


class Statistics(View):
    def get(self, request, *args, **kwargs):

        managers_count = User.objects.filter(is_manager=True).count()
        total_borrowed_items = BorrowItem.objects.all().count()
        total_late_returned_items = [item for item in BorrowItem.objects.exclude(hand_in_date=None) if item.hand_in_date > (datetime.timedelta(days=item.item.borrow_days) + item.borrow_date)]
        current_borrowing_items = BorrowItem.objects.filter(hand_in_date=None).count()
        total_different_items = Item.objects.all().count()

        # TODO: all lists of data that should be added.
        '''
        managers
        list of returned items
        list of available items
        list of broken items
        list of different people that have borrowed an item.
        top 5 most borrowed items
        '''

        return JsonResponse(json.loads(json.dumps({
            'managers': managers_count,
            'total_borrowed_items': total_borrowed_items,
            'total_late_returned_items': len(total_late_returned_items),
            'current_borrow_items': current_borrowing_items,
            'total_different_items': total_different_items
        })), safe=False, content_type='application/json')
