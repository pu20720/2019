# -*- coding: utf-8 -*-
# Generated by Django 1.11.11 on 2019-11-12 13:05
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('records', '0011_auto_20191112_1924'),
    ]

    operations = [
        migrations.AddField(
            model_name='records',
            name='Picture_path',
            field=models.CharField(max_length=999, null=True, verbose_name='圖片存放路徑'),
        ),
        migrations.AlterField(
            model_name='records',
            name='Picture',
            field=models.ImageField(blank=True, upload_to='photo', verbose_name='個人圖片'),
        ),
    ]
