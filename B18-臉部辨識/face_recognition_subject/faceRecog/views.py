from .settings import BASE_DIR
from django.shortcuts import render, redirect

import os
import re
import cv2
import sys
import copy
import math
import time
import json
import shutil
import base64
import pickle
import random
import logging
import imutils
import argparse
import numpy as np
from PIL import Image
from time import time
from tqdm import tqdm
from scipy import misc
import tensorflow as tf
from data import facenet
from sklearn.svm import SVC
from data import detect_face
from keras.layers import Dense
import matplotlib.pyplot as plt
from keras.datasets import mnist
from keras.utils import np_utils
from . import dataset_fetch as df
from sklearn.svm import LinearSVC
from os.path import join as pjoin
from records.models import Records
from scipy.spatial import distance
from keras.models import Sequential
from sklearn.externals import joblib
from imutils.video import VideoStream
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import GridSearchCV
from data import visualization_utils as vis_utils
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split


def index(request):
    return render(request, 'index.html')
def errorImg(request):
    return render(request, 'error.html')
def about_us(request):
    return render(request, 'about_us.html')
def about_object(request):
    return render(request, 'about_object.html')

def create_dataset(request):
    Already_used_img = cv2.imread(BASE_DIR + '/static/img/Already_used.jpg')
    
    
    #--------------------讀取使用者號碼------------------#
    try:
        user = open(BASE_DIR + '/data/user_Num.txt', 'r+')

        lines = user.readlines() #str

        last_line = lines[-1]

        print("讀取到的最後一筆號碼是：" + last_line)   #讀取最後一筆資料

        user_Num = last_line #重新命名方便做使用


        int_Num = int(user_Num) #轉成int
        int_Num = int_Num + 1 #讀取 + 1
        str_Num = str(int_Num) #轉成str


        new_user_Num = str_Num #重新命名方便做使用

        print("這次儲存的號碼是：" + new_user_Num)


        user.write('\n'+str_Num) #儲存

        user.close()
    
    except:
        print('error')
        return redirect('/')
    
    
    id = new_user_Num

    
        #-------------------自動註冊用戶表單--------------------#
    try:
            first_name = 'first_name_' + id    # 自動儲存流水資料
            last_name = 'last_name_' + id
            address = 'address' + id
            grade = 'grade' + id
            Department = 'Department' + id
            Picture = '/static/img/user.' + id + '.10.jpg' # 彩圖位置
            Introduction = id
            unit = Records.objects.create(id = id,
                                          first_name=first_name,
                                          last_name=last_name,
                                          address=address,
                                          grade=grade,
                                          Picture=Picture,
                                          Department=Department,
                                          Introduction=Introduction) 
            unit.save()
    except:
            print("此ID已被使用")
            cv2.imshow('Already_used', Already_used_img)
            cv2.waitKey(2000)
            cv2.destroyAllWindows()
            return redirect('/')
 
    
    # 人臉偵測
    # 選擇haar
    faceDetect = cv2.CascadeClassifier(BASE_DIR+'/data/haar/haarcascade_frontalface_alt2.xml')
    
    cam = cv2.VideoCapture(0)
    
    # 建立userId資料夾
    face_dir = BASE_DIR+'/data/dataset/user'+str(id)
    
    if not os.path.exists(face_dir):
        os.makedirs(face_dir)
    

    sampleNum = 0
    saveNum = 0
    
    #----------------攝像頭捕捉人像-----------------#
    
    while(True):
        # 攝像頭回傳變量、彩圖        
        ret, img = cam.read()
        
        # 將彩圖轉換成灰階圖，獲得更快的運作速度
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        # 儲存人臉
        faces = faceDetect.detectMultiScale(gray, 1.3, 7)

        for(x,y,w,h) in faces:

            # 每獲取一張人臉都會儲存成一張jpg檔，並標上序號
            saveNum = saveNum+1
            sampleNum = sampleNum+1
            
            if(saveNum==10):
                cv2.imwrite(BASE_DIR+'/static/img/user.'+str(id)+'.'+str(saveNum)+'.jpg', img) # 儲存彩圖
                
            # 儲存的人臉會直接裁切保存到前面建立的userId資料夾   
            cv2.imwrite(BASE_DIR+'/data/dataset/user'+str(id)+'/user.'+str(id)+'.'+str(sampleNum)+'.jpg', gray[y:y+h,x:x+w])
            
            # 起始點 (x, y)
            # 終點 (x+width, y+height)
            # 矩形顏色 (0, 255, 0)
            # 矩形寬度 2
            cv2.rectangle(img,(x,y),(x+w,y+h), (0,255,0), 2)
            
            # 100毫秒循環
            cv2.waitKey(100)

        # 另外創建一個視窗方便觀看拍攝畫面
        cv2.imshow("Face",img)

        # Debug
        cv2.waitKey(100)

        # 當拍攝到15張時關閉程式
        if(sampleNum>14):
            break
            
    cam.release()
    cv2.destroyAllWindows()
    
    
    return redirect('/trainer_camera') # 返回頁面

def delete_user(request,id=None):                  #刪除資料
    
    OK_img = cv2.imread(BASE_DIR + '/static/img/OK.jpg')
    NO_img = cv2.imread(BASE_DIR + '/static/img/NO.jpg')
    
    id = request.POST['id']
    print(id)
    if request.method == "POST":
        try:
            unit = Records.objects.get(id=id)  #取得id欄位的資料
            unit.delete()                      #刪除資料       
            delDir = BASE_DIR + "/data/dataset/user" + str(id)
            print(delDir)
            shutil.rmtree(delDir)
        except:
            message = "查無此ID，請重新輸入"
            cv2.imshow('Failed',NO_img)
            cv2.waitKey(2000)
            cv2.destroyAllWindows()
        else:
            unit.save() 
            print("資料修改成功")
            cv2.imshow('Successful',OK_img)
            cv2.waitKey(2000)
            cv2.destroyAllWindows()
            return redirect('/trainer_camera/')
            
    return redirect('/')

def user_information(request,id=None):
    
    OK_img = cv2.imread(BASE_DIR + '/static/img/OK.jpg')
    NO_img = cv2.imread(BASE_DIR + '/static/img/NO.jpg')
    
    id = request.POST['id']
    print(id)
        
    if request.method == "POST": 
        try:
            unit = Records.objects.get(id = id)  #取得要修改的資料紀錄
            unit.first_name = request.POST['first_name']    #取得表單輸入資料
            unit.last_name = request.POST['last_name']
            unit.address = request.POST['address']
            unit.grade = request.POST['grade']
            unit.Department = request.POST['Department']
            unit.Introduction = request.POST['Introduction']
        except:
            print("查無此ID，請重新輸入")
            cv2.imshow('Failed',NO_img)
            cv2.waitKey(2000)
            cv2.destroyAllWindows()
        else:
            unit.save() 
            print("資料修改成功")
            cv2.imshow('Successful',OK_img)
            cv2.waitKey(2000)
            cv2.destroyAllWindows()
        
    return redirect('/')


def trainer_camera(request):

    #-------------載入相關模型--------------#
    
    recognizer = cv2.face.LBPHFaceRecognizer_create()
    
    path = BASE_DIR + '/data/dataset'
    
    DATA_PATH = os.path.join(BASE_DIR, "data")
    IMG_OUT_PATH = os.path.join(DATA_PATH, "dataset")
    datadir = IMG_OUT_PATH
    
    dataset = facenet.get_dataset(datadir)
    
    #---------------取得所有圖像與相應ids----------------#
    
    def getImagesWithID(path):
        
        
        imagePaths, labels, labels_dict = facenet.get_image_paths_and_labels(dataset) 
        #print(imagePaths) 路徑
        #print(labels) 數量
        #print(labels_dict) ids
        
        faces = []
        Ids = []
        
        for imagePath in imagePaths:
            # 開啟圖像轉換成numpy
            faceImg = Image.open(imagePath).convert('L') # 轉灰階
            # PIL圖像轉換為numpy
            faceNp = np.array(faceImg, 'uint8')
            
            # 從圖片名稱中獲取使用者ID
            ID = int(os.path.split(imagePath)[-1].split('.')[1]) 
            
            # iamge
            faces.append(faceNp)
            # ids
            Ids.append(ID)
            
            #print ID
            cv2.imshow("training", faceNp)
            cv2.waitKey(10)
        return np.array(Ids), np.array(faces) # 回傳ids faces

    # 取得ids faces資料
    ids, faces = getImagesWithID(path)

    # ids比對faces
    recognizer.train(faces, ids)

    # 儲存訓練集
    recognizer.save(BASE_DIR+'/data/recognizer/trainingData.yml')
    cv2.destroyAllWindows()

    return redirect('/trainer_photo')


def detect(request):
    
    #-----------------載入相關模型-------------------#
    
    faceDetect = cv2.CascadeClassifier(BASE_DIR+'/data/haar/haarcascade_frontalface_alt2.xml')
    
    cam = cv2.VideoCapture(0)
    # creating recognizer
    rec = cv2.face.LBPHFaceRecognizer_create();
    
    rec.read(BASE_DIR+'/data/recognizer/trainingData.yml')
    
    #-----------------設定相關變數--------------------#
    
    getId = 0
    font = cv2.FONT_HERSHEY_SIMPLEX
    userId = 0
    
    #-------------------開始辨識---------------------#
    
    while(True):
        ret, img = cam.read() # 讀取攝像頭
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        faces = faceDetect.detectMultiScale(gray, 1.3, 3)
        for(x,y,w,h) in faces:
            cv2.rectangle(img,(x,y),(x+w,y+h), (0,255,0), 2)

            getId,conf = rec.predict(gray[y:y+h, x:x+w]) # 偵測頭像比對資料庫id

            # 拍攝頭像
            if conf<35:
                userId = getId
                cv2.putText(img, "Detected",(x,y+h), font, 2, (0,255,0),2)
            else:
                cv2.putText(img, "Unknown",(x,y+h), font, 2, (0,0,255),2)

        
        # 進入個人介紹
        cv2.imshow("Face",img)
        if(cv2.waitKey(1) == ord('q')):
            break
        elif(userId != 0):
            cv2.waitKey(1000)
            cam.release()
            cv2.destroyAllWindows()
            return redirect('/records/details/'+str(userId))

    cam.release()
    cv2.destroyAllWindows()
    return redirect('/')

def trainer_photo(request):
    DATA_PATH = os.path.join(BASE_DIR, "data") #data目錄

    FACENET_DATA_PATH = os.path.join(DATA_PATH, "facenet","20180402-114759","20180402-114759.pb") #dacenet路徑

    SVM_DATA_PATH = os.path.join(DATA_PATH, "serializer", "lfw_svm_classifier.pkl") #svm路徑

    IMG_OUT_PATH = os.path.join(DATA_PATH, "dataset") #裁切後人臉目錄
   

    #----------載入MTCNN-----------#
    with tf.Graph().as_default():
        with tf.Session() as sess:
            datadir = IMG_OUT_PATH # 經過偵測、對齊 & 裁剪後的人臉圖像目錄
            
            dataset = facenet.get_dataset(datadir) # 取得人臉類別(ImageClass)的列表與圖像路徑

            paths, labels, labels_dict = facenet.get_image_paths_and_labels(dataset) #取得每個人臉的圖像路徑跟ID標籤
            #print (paths) #test
            #print (labels) #test
            #print (labels_dict) #test
            print('Origin: Number of classes: %d' % len(labels_dict)) #人臉種類
            print('Origin: Number of images: %d' % len(paths)) #人臉總數
            
            #------------載入Facenet模型------------#
            modeldir =  FACENET_DATA_PATH
            facenet.load_model(modeldir)

            images_placeholder = tf.get_default_graph().get_tensor_by_name("input:0")
            embeddings = tf.get_default_graph().get_tensor_by_name("embeddings:0")
            phase_train_placeholder = tf.get_default_graph().get_tensor_by_name("phase_train:0")
            embedding_size = embeddings.get_shape()[1]

            #------------計算人臉特徵向量------------#
            batch_size = 3 # 一次輸入的樣本數量
            image_size = 140  # 要做為Facenet的圖像輸入的大小            
            times_pohto = 10.0  # 每張照片看的次數
            nrof_images = len(paths) # 總共要處理的人臉圖像 
            # 計算總共要跑的批次數
            nrof_batches_per_epoch = int(math.ceil(times_pohto * nrof_images / batch_size))
            # 構建一個變數來保存"人臉特徵向量"
            emb_array = np.zeros((nrof_images, embedding_size)) # <-- Face Embedding
            
            for i in tqdm(range(nrof_batches_per_epoch)): # 實際訓練 facenet
                start_index = i * batch_size
                end_index = min((i + 1) * batch_size, nrof_images)
                paths_batch = paths[start_index:end_index]
                images = facenet.load_data(paths_batch, False, False, image_size)
                feed_dict = {images_placeholder: images, phase_train_placeholder: False}
                emb_array[start_index:end_index, :] = sess.run(embeddings, feed_dict=feed_dict)
           


    #--------------保存facenet.pkl-------------#          
    # 人臉特徵
    emb_features_file = open(os.path.join(DATA_PATH+'/recognizer', 'lfw_emb_features.pkl'), 'wb')
    pickle.dump(emb_array, emb_features_file)
    emb_features_file.close()

    # 矩陣
    emb_lables_file = open(os.path.join(DATA_PATH+'/recognizer', 'lfw_emb_labels.pkl'), 'wb')
    pickle.dump(labels, emb_lables_file)
    emb_lables_file.close()

    # user_ids
    emb_lables_dict_file = open(os.path.join(DATA_PATH+'/recognizer', 'lfw_emb_labels_dict.pkl'), 'wb')
    pickle.dump(labels_dict, emb_lables_dict_file)
    emb_lables_dict_file.close()


    #------------------載入pkl------------------#

    # 人臉特徵
    with open(os.path.join(DATA_PATH+'/recognizer', 'lfw_emb_features.pkl'), 'rb') as emb_features_file:
        emb_features =pickle.load(emb_features_file)

    # 每一張的人臉標籤
    with open(os.path.join(DATA_PATH+'/recognizer', 'lfw_emb_labels.pkl'), 'rb') as emb_lables_file:
        emb_labels =pickle.load(emb_lables_file)

    # 總共的人臉標籤種類
    with open(os.path.join(DATA_PATH+'/recognizer', 'lfw_emb_labels_dict.pkl'), 'rb') as emb_lables_dict_file:
        emb_labels_dict =pickle.load(emb_lables_dict_file)
        
    #-------------------測試--------------------#    
    
    print("人臉特徵數量: {}, shape: {}, type: {}".format(len(emb_features), emb_features.shape, type(emb_features)))
    print("人臉標籤數量: {}, type: {}".format(len(emb_labels), type(emb_labels)))
    print("人臉標籤種類: {}, type: {}", len(emb_labels_dict), type(emb_labels_dict))

    
    #-------------------準備相關變數-----------------#
    
    # 訓練/測試變數
    X_train = []; y_train = []
    X_test = []; y_test = []

    # 保存己經有處理過的人臉label
    processed = set()

    # 分割訓練資料集與驗證資料集
    for (emb_feature, emb_label) in zip(emb_features, emb_labels):
        if emb_label in processed:
            X_train.append(emb_feature)
            y_train.append(emb_label)
        else:
            X_test.append(emb_feature)
            y_test.append(emb_label)
            processed.add(emb_label)

    # 結果
    print('X_train: {}, y_train: {}'.format(len(X_train), len(y_train)))
    print('X_test: {}, y_test: {}'.format(len(X_test), len(y_test)))


    #----------------訓練人臉分類器(SVM Classifier)-----------------#
    #使用scikit-learn的SVM分類器來進行訓練。
    #使用linearSvc來訓練

    # 訓練分類器
    print('Training classifier')
    linearsvc_classifier = LinearSVC(C=1, multi_class='crammer_singer')

    # 進行訓練
    linearsvc_classifier.fit(X_train, y_train)

    classifier_filename = SVM_DATA_PATH


    class_names = []
    for key in sorted(emb_labels_dict.keys()):
        class_names.append(emb_labels_dict[key].replace('_', ' '))


    with open(classifier_filename, 'wb') as outfile:
        pickle.dump((linearsvc_classifier, class_names), outfile)

    print('Saved classifier model to file "%s"' % classifier_filename)
    
    return redirect('/')

face_id_name = ''

def detectImage(request):  
    userImage = request.FILES['userImage']
    
    

    DATA_PATH = os.path.join(BASE_DIR, "data")
    # MTCNN的模型
    MTCNN_DATA_PATH = os.path.join(DATA_PATH, "mtcnn")
    # FaceNet的模型
    FACENET_DATA_PATH = os.path.join(DATA_PATH, "facenet","20180402-114759","20180402-114759.pb")
    # Classifier的模型
    SVM_DATA_PATH = os.path.join(DATA_PATH, "serializer", "lfw_svm_classifier.pkl")
    # 訓練/驗證用的圖像資料目錄
    IMG_OUT_PATH = os.path.join(DATA_PATH, "dataset")
    
    
    #-------------------載入--------------------#
    
    # 人臉特徵
    with open(os.path.join(DATA_PATH+'/recognizer','lfw_emb_features.pkl'), 'rb') as emb_features_file:
        emb_features =pickle.load(emb_features_file)
        # print(emb_features)

    # 矩陣
    with open(os.path.join(DATA_PATH+'/recognizer','lfw_emb_labels.pkl'), 'rb') as emb_lables_file:
        emb_labels =pickle.load(emb_lables_file)
        # emb_labels

    # user_ids
    with open(os.path.join(DATA_PATH+'/recognizer','lfw_emb_labels_dict.pkl'), 'rb') as emb_lables_dict_file:
        emb_labels_dict =pickle.load(emb_lables_dict_file)
        # emb_labels_dict
        
    emb_dict = {} # key 是label, value是embedding list
    for feature,label in zip(emb_features, emb_labels):
        # 檢查key有沒有存在
        if label in emb_dict:
            emb_dict[label].append(feature)
        else:
            emb_dict[label] = [feature]
            
        # 計算兩個人臉特徵（Facenet Embedding 128 bytes vector)的歐式距離
    def calc_dist(face1_emb, face2_emb):    
        return distance.euclidean(face1_emb, face2_emb)

    face_distance_threshold = 1.1



    def is_same_person(face_emb, face_label, threshold = 1.1):
        emb_distances = []
        emb_features = emb_dict[face_label]
        for i in range(len(emb_features)):
            emb_distances.append(calc_dist(face_emb, emb_features[i]))

        # 取得平均值
        if np.mean(emb_distances) > threshold:
            return False
        else:
            return True
    
    #-------------------MTCNN相關變數-------------------#
    
    minsize = 20
    threshold = [0.6, 0.7, 0.7]  # 三個網絡(P-Net, R-Net, O-Net)的閥值
    factor = 0.709

    margin = 44 # 在裁剪人臉時的邊框margin
    image_size = 182 

    batch_size = 1000
    input_image_size = 160
    
    #--------------------載入模型---------------------#

    # 創建Tensorflow Graph物件
    tf.reset_default_graph()
    gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=0.8) # 將GPU的顯存設為60%

    # 創建Tensorflow Session物件
    tf_sess = tf.Session(config=tf.ConfigProto(gpu_options=gpu_options, log_device_placement=False)) # False 不打印設備分配紀錄

    # 把session設為預設
    tf_sess.as_default()
    
    
    # 載入MTCNN模型 (偵測人臉位置)
    pnet, rnet, onet = detect_face.create_mtcnn(tf_sess, MTCNN_DATA_PATH)

    
    # 載入Facenet模型
    print('Loading feature extraction model')
    modeldir =  FACENET_DATA_PATH
    facenet.load_model(modeldir)

    # 取得模型的輸入與輸出的佔位符
    images_placeholder = tf.get_default_graph().get_tensor_by_name("input:0")
    embeddings = tf.get_default_graph().get_tensor_by_name("embeddings:0")
    phase_train_placeholder = tf.get_default_graph().get_tensor_by_name("phase_train:0")
    embedding_size = embeddings.get_shape()[1]

    # 打印"人臉特徵向量"的向量大小
    print("Face embedding size: ", embedding_size)
    
    
    # 載入SVM分類器模型
    classifier_filename = SVM_DATA_PATH

    with open(classifier_filename, 'rb') as svm_model_file:
        (face_svc_classifier, face_identity_names) = pickle.load(svm_model_file)
        HumanNames = face_identity_names    #訓練時的人臉的身份

        print('load classifier file-> %s' % classifier_filename)
        #測試是否成功載入
        # print(face_svc_classifier)
    
    #-----------------------開始辨識---------------------------# 
        
    print('Start Recognition!')
    
    im = Image.open(userImage)
    #im.show()
    imgPath = BASE_DIR+'/data/uploadedImages/'+str(userImage)
    im.save(imgPath, 'JPEG')

    face_input = imgPath

    find_results = []
    frame = cv2.imread(face_input) # 讀入圖像
    draw = frame.copy() # 複製原圖像


    frame = frame[:,:,::-1] # 把BGR轉換成RGB
    # 偵測人臉位置
    # 偵測人臉的邊界框
    bounding_boxes, _ = detect_face.detect_face(frame, minsize, pnet, rnet, onet, threshold, factor)
    nrof_faces = bounding_boxes.shape[0] # 被偵測到的臉部總數
    if nrof_faces > 0: # 如果有偵測到人臉
        # 每一個 bounding_box包括了（x1,y1,x2,y2,confidence score)：
        # 　　左上角座標 (x1,y1)
        #     右下角座標 (x2,y2)
        #     信心分數 confidence score
        det = bounding_boxes[:, 0:4].astype(int) # 取出邊界框座標
        img_size = np.asarray(frame.shape)[0:2] # 原圖像大小 (height, width)

        print("Image: ", img_size)

        # 人臉圖像前處理的暫存
        cropped = []
        scaled = []
        scaled_reshape = []
        bb = np.zeros((nrof_faces,4), dtype=np.int32)

        # 擷取人臉特徵
        for i in range(nrof_faces):
            print("faces#{}".format(i))
            emb_array = np.zeros((1, embedding_size))

            x1 = bb[i][0] = det[i][0]
            y1 = bb[i][1] = det[i][1]
            x2 = bb[i][2] = det[i][2]
            y2 = bb[i][3] = det[i][3]

            print('({}, {}) : ({}, {})'.format(x1,y1,x2,y2)) #檢查人臉座標
            if bb[i][0] <= 0 or bb[i][1] <= 0 or bb[i][2] >= len(frame[0]) or bb[i][3] >= len(frame): #人臉超出範圍
                print('face is out of range!')
                continue


            # **人臉圖像的前處理 **

            # 根據邊界框的座標來進行人臉的裁剪
            cropped.append(frame[bb[i][1]:bb[i][3], bb[i][0]:bb[i][2], :])
            cropped[i] = facenet.flip(cropped[i], False)
            scaled.append(misc.imresize(cropped[i], (image_size, image_size), interp='bilinear'))
            scaled[i] = cv2.resize(scaled[i], (input_image_size,input_image_size),
                                   interpolation=cv2.INTER_CUBIC)
            scaled[i] = facenet.prewhiten(scaled[i])
            scaled_reshape.append(scaled[i].reshape(-1,input_image_size,input_image_size,3))       
            feed_dict = {images_placeholder: scaled_reshape[i], phase_train_placeholder: False}

            # 進行臉部特徵擷取
            emb_array[0, :] = tf_sess.run(embeddings, feed_dict=feed_dict)

            # 進行人臉識別分類
            face_id_idx = face_svc_classifier.predict(emb_array) 
            print(face_id_idx)

            if is_same_person(emb_array, int(face_id_idx), 0.7):
                face_id_name = HumanNames[int(face_id_idx)] # 取出人臉的名字
            else:
                print("No face detected, or image not recognized")
                return redirect('/error_image') #檢測失敗
    else:
        print('Unable to align')


    
    
    ids = face_id_name.strip('user')
    
    
    return redirect('/records/details/'+str(ids))
