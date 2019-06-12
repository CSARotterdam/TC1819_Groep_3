from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

from api.models import (API)

import json
from django.contrib.auth import authenticate

# Create your views here.
@method_decorator(csrf_exempt, name='dispatch')
class Login(View):

    def post(self, request, *args, **kwargs):
        if (i in request.POST for i in ['username', 'password']):
            user = authenticate(username=request.POST.get('username'), password=request.POST.get('password'))
            if user is not None:
                user_api, created = API.objects.get_or_create(user=user)

                return JsonResponse(json.loads('{"success": "success", "api_token":"%s"}' % user_api.api_key),
                                    safe=False, status=200, content_type='application/json')
            else:
                return JsonResponse(json.loads('{"success": "wrong_user_pass", "message":"Username or password invalid"}'),
                                    safe=False, status=401, content_type='application/json')

        return JsonResponse(json.loads('{"success": "invalid_parameters", "message":"Missing username or password"}'),
                            safe=False, status=403, content_type='application/json')
