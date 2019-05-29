from django.contrib import admin
from django.conf.urls import url, include
from django.views.decorators.csrf import csrf_exempt

from .views import (GetAllItems,
                    GetFilteredItems,
                    AdminLogin,
                    BorrowItems,
                    Managers,
                    Statistics,
                    Manufacturer,
                    Writer,
                    Publisher,
                    Category)

# TODO: Add user permissions.
'''
def function():
    if 'email' in request.POST:
        manager = User.objects.filter(email=request.POST['email'], is_manager=True).first() if \
            User.objects.filter(email=request.POST['email'], is_manager=True).count() > 0 else None

        if manager is not None:
            pass
'''

urlpatterns = [
    url(r'^items/$', GetAllItems.GetAllItems.as_view()),
    url(r'^items/(?P<pk>[0-9a-f-]+)/$', GetAllItems.GetItem.as_view()),
    url('items/filtered/', GetFilteredItems.GetFilteredItems.as_view()),
    url(r'^electronics/$', csrf_exempt(GetAllItems.GetAllElectronics.as_view())),
    url(r'^electronics/(?P<pk>[0-9a-f-]+)/$', GetFilteredItems.GetFilteredElectronics.as_view()),
    url(r'^books/$', csrf_exempt(GetAllItems.GetAllBooks.as_view())),
    url(r'^books/(?P<pk>[0-9a-f-]+)/$', GetFilteredItems.GetFilteredBooks.as_view()),
    url(r'^borrowitems/$', BorrowItems.BorrowItems.as_view()),
    url(r'^borrowitems/(?P<pk>[0-9a-f-]+)/$', csrf_exempt(BorrowItems.BorrowItems.as_view())),
    url(r'^returnitems/$', BorrowItems.ReturnItems.as_view()),
    url(r'managers/$', Managers.Managers.as_view()),
    url(r'managers/(?P<pk>[0-9a-f-]+)/$', csrf_exempt(Managers.Manager.as_view())),
    # TODO: contact -> POST{user, issue, message} WOULD
    # url(r'^contact/', ),
    url(r'^statistics/', Statistics.Statistics.as_view()),
    url(r'^manufacturers/', Manufacturer.Manufacturers.as_view()),
    url(r'^writers/', Writer.Writers.as_view()),
    url(r'^categories/', Category.Categories.as_view()),
    url(r'^publishers/', Publisher.Publishers.as_view()),
]
