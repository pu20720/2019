
"""faceRecog URL Configuration
The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url, include
from django.contrib import admin
from faceRecog import views as app_views

urlpatterns = [
    url(r'^$', app_views.index),
    url(r'^about_us/', app_views.about_us),
    url(r'^about_object/', app_views.about_object),
    url(r'^detect/$', app_views.detect),
    url(r'^error_image/$', app_views.errorImg),
    url(r'^delete_user/$', app_views.delete_user),
    url(r'^detect_image$', app_views.detectImage),
    url(r'^trainer_photo/$', app_views.trainer_photo),
    url(r'^create_dataset/$', app_views.create_dataset),
    url(r'^trainer_camera/$', app_views.trainer_camera),
    url(r'^user_information/$', app_views.user_information),    
    url(r'^admin/', admin.site.urls),
    url(r'^records/', include('records.urls')),
]