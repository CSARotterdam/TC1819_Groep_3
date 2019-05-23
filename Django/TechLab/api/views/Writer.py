from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (Writer)

import json


class Writers(View):
    def get(self, request, *atgs, **kwargs):

        allWriters = Writer.objects.all()

        return JsonResponse(json.loads(
            json.dumps([writer.to_json('_state') for writer in allWriters]),
        ), safe=False)
