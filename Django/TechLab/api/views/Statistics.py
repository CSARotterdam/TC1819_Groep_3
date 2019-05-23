from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (User)
import json


class Statistics(View):
    def get(self, request, *args, **kwargs):

        managers_count = User.objects.filter(is_manager=True).count()

        # TODO: all lists of data that should be added.
        '''
        managers
        list of currently borrowed items
        list of returned items
        list of available items
        list of different people that have borrowed an item.
        
        '''

        return JsonResponse(json.loads(json.dumps({
            'managers': managers_count
        })), safe=False)
