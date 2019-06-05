from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (Publisher)

import json


class Publishers(View):
    def get(self, request, *atgs, **kwargs):

        allPublishers = Publisher.objects.all()

        return JsonResponse(json.loads(
            json.dumps([publisher.to_json('_state') for publisher in allPublishers]),
        ), safe=False, content_type='application/json')
