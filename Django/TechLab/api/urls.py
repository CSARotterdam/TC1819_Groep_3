from django.contrib import admin
from django.conf.urls import url, include
from django.views.decorators.csrf import csrf_exempt

from .views import (GetAllItems, GetFilteredItems, AdminLogin, BorrowItems, Managers)

urlpatterns = [
    url(r'^items/$', GetAllItems.GetAllItems.as_view()),
    url('items/filtered/', GetFilteredItems.GetFilteredItems.as_view()),
    url(r'^electronics/$', GetAllItems.GetAllElectronics.as_view()),
    url(r'^electronics/(?P<pk>[0-9a-f-]+)/$', GetFilteredItems.GetFilteredElectronics.as_view()),
    url(r'^books/$', GetAllItems.GetAllBooks.as_view()),
    url(r'^books/(?P<pk>[0-9a-f-]+)/$', GetFilteredItems.GetFilteredBooks.as_view()),
    url(r'^borrowitems/$', BorrowItems.BorrowItems.as_view()),
    url(r'^borrowitems/(?P<pk>[0-9a-f-]+)/$', csrf_exempt(BorrowItems.BorrowItems.as_view())),
    url(r'^returnitems/$', BorrowItems.ReturnItems.as_view()),
    url(r'managers/$', Managers.Managers.as_view()),
    url(r'managers/(?P<pk>[0-9a-f-]+)/$', csrf_exempt(Managers.Manager.as_view())),
    # url(r'^statistics/', AdminLogin.Login.as_view()),
    # url(r'^manufacturers/', AdminLogin.Login.as_view()),
    # url(r'^writers/', AdminLogin.Login.as_view()),
    # url(r'^categories/', AdminLogin.Login.as_view()),
    # url(r'^publishers/', AdminLogin.Login.as_view()),
]
