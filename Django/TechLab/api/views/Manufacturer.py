from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (Manufacturer)

import json


class Manufacturers(View):
    def get(self, request, *atgs, **kwargs):

        allManufacturers = Manufacturer.objects.all()

        return JsonResponse(json.loads(
            json.dumps([manufacturer.to_json('_state') for manufacturer in allManufacturers]),
        ), safe=False, content_type='application/json')
