from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (Category)

import json


class Categories(View):
    def get(self, request, *atgs, **kwargs):

        allCategories = Category.objects.all()

        return JsonResponse(json.loads(
            json.dumps([category.to_json('_state') for category in allCategories]),
        ), safe=False, content_type='application/json')
