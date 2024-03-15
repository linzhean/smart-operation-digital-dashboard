from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('products/', include('dashboard.urls')),
    path('accounts/', include('allauth.urls')),
    path('admin/', admin.site.urls),
]
