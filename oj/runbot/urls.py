from django.urls import path
from runbot.views.fight import fight
from runbot.views.compile import compile
urlpatterns = [
    path("", fight, name="index"),
    path("compile/", compile, name="compile")
]
