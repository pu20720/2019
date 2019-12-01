# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from datetime import datetime
from django.db import models



# 建立個人資料
class Records(models.Model):
    id = models.CharField(max_length=100, primary_key=True)
    first_name = models.CharField('姓', max_length=50)
    last_name = models.CharField('名', max_length=50, null=True)
    address = models.CharField('住址', max_length=50, null=True)
    grade = models.CharField('年級', max_length=150, null=True)
    Department = models.CharField('系所', max_length=150, null=True)
    Picture = models.ImageField('個人圖片', upload_to='', blank=True)
    Introduction = models.TextField('個人介紹')
    recorded_at = models.DateTimeField('資料儲存時間',default=datetime.now, blank=True)

    def __str__(self):
        return self.first_name   
    

    class Meta:
        verbose_name_plural = "Records"
