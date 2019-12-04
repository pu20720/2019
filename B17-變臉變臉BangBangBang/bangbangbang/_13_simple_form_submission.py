from flask import Flask, request, render_template
import os

from PIL import Image, ImageDraw
import face_recognition

app = Flask(__name__)

# 相片位置
app.config['EYEBROW'] = os.path.join('static', 'shape')
app.config['RESULT_LIP'] = os.path.join('static', 'lip')
app.config['RESULT_EYEBROW'] = os.path.join('static', 'eyebrow')
app.config['RESULT_MIX'] = os.path.join('static', 'mix')


# 相片位置
# app.config['UPLOAD_FOLDER'] = os.path.join('static', 'people_photo')

def toNum(c):
    if '0' <= c <= '9':
        return int(c)
    else:
        return int(c - 'a' + 10)


@app.route('/', methods=['POST', 'GET'])
def hello_world():
    if request.method == 'GET':
        return render_template('base.html')
    elif request.method == 'POST':
        if request.form['submit'] == 'eyebrow':
            return render_template('forms/eyebrow.html')
        elif request.form['submit'] == 'mix':
            return render_template('forms/mix.html')
        elif request.form['submit'] == 'lip':
            return render_template('forms/lip.html')
        elif request.form['submit'] == 'back':
            return render_template('base.html')
        elif request.form['submit'] == 'resLip':
            # image = request.files['pic']
            # print(image)
            # full_filename = os.path.join(app.config["UPLOAD_FOLDER"], image.filename)
            # image.save(full_filename)
            # return render_template("forms/uploadImage_result.html", image=full_filename)

            # 照片儲存與顯示
            # full_filename = os.path.join(app.config["UPLOAD_FOLDER"], image.filename)
            # image.save(full_filename)
            # return render_template("forms/uploadImage_result.html", image=full_filename)

            # 顯示相片在畫面上
            # full_filename = os.path.join(app.config['UPLOAD_FOLDER'], 'Li.jpg')
            # return render_template("forms/uploadImage_result.html", image=full_filename)

            # 取得照片檔案
            image_path = request.files['pic']

            # 將相片檔案存到特定的資料夾
            full_filename1 = os.path.join(app.config["RESULT_LIP"], image_path.filename)
            image_path.save(full_filename1)

            # 取得使用者選擇口紅的顏色
            color = request.form['color']
            r = int('0x' + color[0] + color[1], 16)
            g = int('0x' + color[2] + color[3], 16)
            b = int('0x' + color[4] + color[5], 16)

            # 載入使用者所選取的相片
            image = face_recognition.load_image_file(image_path)

            # Find all facial features in all the faces in the image
            face_landmarks_list = face_recognition.face_landmarks(image)

            pil_image = Image.fromarray(image)
            for face_landmarks in face_landmarks_list:
                d = ImageDraw.Draw(pil_image, 'RGBA')

                # 為嘴唇上色
                d.polygon(face_landmarks['top_lip'], fill=(r, g, b, 128))
                d.polygon(face_landmarks['bottom_lip'], fill=(r, g, b, 128))
                d.line(face_landmarks['top_lip'], fill=(r, g, b, 64), width=4)
                d.line(face_landmarks['bottom_lip'], fill=(r, g, b, 64), width=4)

            # 將上完口紅的照片存到lip的資料夾
            full_filename = os.path.join(app.config['RESULT_LIP'], color + "_" + image_path.filename)
            pil_image.save(full_filename)

            # pil_image.show()

            # 將圖片的檔名傳給return給網頁
            return render_template("forms/uploadImage_result.html", image=full_filename1, result=full_filename)

        elif request.form['submit'] == 'resEyebrow':
            # 取得相片檔案、眉毛的編號
            image_path = request.files['pic']
            eyebrow = request.form['shape']

            # 將相片檔案存到特定的資料夾
            full_filename1 = os.path.join(app.config["RESULT_EYEBROW"], image_path.filename)
            image_path.save(full_filename1)

            # 載入使用者所選取的相片
            image = face_recognition.load_image_file(image_path)

            #  Find all facial features in all the faces in the image
            image_landmarks = face_recognition.face_landmarks(image)

            # 取得使用者所選眉毛的檔案名稱
            full_filenameL = os.path.join(app.config["EYEBROW"], eyebrow + "-l.png")
            full_filenameR = os.path.join(app.config["EYEBROW"], eyebrow + "-r.png")

            leftL = 0
            rightL = 0
            topL = 0
            bottomL = 0

            leftR = 0
            rightR = 0
            topR = 0
            bottomR = 0

            pil_image = Image.fromarray(image)

            for face_landmarks in image_landmarks:

                first = True

                # 抓到左邊眉毛最小的長方形
                for point in face_landmarks['left_eyebrow']:
                    if first:
                        leftL = point[0]
                        rightL = point[0]
                        topL = point[1]
                        bottomL = point[1]
                        first = False
                    else:
                        if point[0] < leftL:
                            leftL = point[0]
                        if point[0] > rightL:
                            rightL = point[0]
                        if point[1] < topL:
                            topL = point[1]
                        if point[1] > bottomL:
                            bottomL = point[1]

                first = True
                # 抓到右邊眉毛最小的長方形
                for point in face_landmarks['right_eyebrow']:
                    if first:
                        leftR = point[0]
                        rightR = point[0]
                        topR = point[1]
                        bottomR = point[1]
                        first = False
                    else:
                        if point[0] < leftR:
                            leftR = point[0]
                        if point[0] > rightR:
                            rightR = point[0]
                        if point[1] < topR:
                            topR = point[1]
                        if point[1] > bottomR:
                            bottomR = point[1]

                print("臉部特徵({})座標點：{}".format('left_eyebrow', face_landmarks['left_eyebrow']))

                # 左眉毛由上往下圖上皮膚的顏色
                for y in range(topL - 5, bottomL + 2):
                    # for y in range(bottomL+2, topL-5, -1): 由下往上
                    for x in range(leftL + 3, rightL + 2):
                        pil_image.putpixel((x, y), pil_image.getpixel((x, y - 1)))

                # 右眉毛由上往下圖上皮膚的顏色
                for y in range(topR - 5, bottomR + 2):
                    for x in range(leftR + 3, rightR + 2):
                        pil_image.putpixel((x, y), pil_image.getpixel((x, y - 1)))

            # 開啟照片
            img = pil_image
            img = img.convert('RGBA')

            # 開啟左眉毛
            eyebrowL = Image.open(full_filenameL)
            eyebrowL = eyebrowL.convert('RGBA')

            # 開啟右眉毛
            eyebrowR = Image.open(full_filenameR)
            eyebrowR = eyebrowR.convert('RGBA')

            # 新建一個透明的底圖
            resultPicture = Image.new('RGBA', img.size, (0, 0, 0, 0))
            # 把照片貼到底圖
            resultPicture.paste(img, (0, 0))

            for face_landmarks in image_landmarks:

                first = True

                # 抓到左邊眉毛最小的長方形
                for point in face_landmarks['left_eyebrow']:
                    if first:
                        leftL = point[0]
                        rightL = point[0]
                        topL = point[1]
                        bottomL = point[1]
                        first = False
                    else:
                        if point[0] < leftL:
                            leftL = point[0]
                        if point[0] > rightL:
                            rightL = point[0]
                        if point[1] < topL:
                            topL = point[1]
                        if point[1] > bottomL:
                            bottomL = point[1]

                first = True
                # 抓到右邊眉毛最小的長方形
                for point in face_landmarks['right_eyebrow']:
                    if first:
                        leftR = point[0]
                        rightR = point[0]
                        topR = point[1]
                        bottomR = point[1]
                        first = False
                    else:
                        if point[0] < leftR:
                            leftR = point[0]
                        if point[0] > rightR:
                            rightR = point[0]
                        if point[1] < topR:
                            topR = point[1]
                        if point[1] > bottomR:
                            bottomR = point[1]

                # 重設的寬為原眉毛的寬度
                newWidthL = rightL - leftL
                # 重設的高依據原眉毛的高
                newHeightL = bottomL - topL
                # 重設左眉毛圖片
                imageL_resize = eyebrowL.resize((newWidthL, newHeightL))

                # 重設的寬為原眉毛的寬度
                newWidthR = rightR - leftR
                # 重設的高依據原眉毛的高
                newHeightR = bottomR - topR
                # 重設右眉毛圖片
                imageR_resize = eyebrowR.resize((newWidthR, newHeightR))

                # 設定簽名檔的位置參數
                right_bottomL = (leftL, topL)
                right_bottomR = (leftR, topR)

                # 為了背景保留透明度，將im參數與mask參數皆帶入重設過後的簽名檔圖片
                resultPicture.paste(imageL_resize, right_bottomL, imageL_resize)
                resultPicture.paste(imageR_resize, right_bottomR, imageR_resize)

            # resultPicture.show()

            # 將置換完眉毛的照片以.png檔存到eyebrow的資料夾中
            length = len(image_path.filename)
            filename = request.form['shape'] + "_" + image_path.filename[0:length - 4] + ".png"
            full_filename = os.path.join(app.config['RESULT_EYEBROW'], filename)
            resultPicture.save(full_filename)

            return render_template("forms/uploadImage_result.html", image=full_filename1, result=full_filename)
        else:

            # 取得照片檔案
            image_path = request.files['pic']

            # 將相片檔案存到特定的資料夾
            full_filename1 = os.path.join(app.config["RESULT_MIX"], image_path.filename)
            image_path.save(full_filename1)

            # 取得使用者選擇口紅的顏色
            color = request.form['color']
            r = int('0x' + color[0] + color[1], 16)
            g = int('0x' + color[2] + color[3], 16)
            b = int('0x' + color[4] + color[5], 16)

            # 載入使用者所選取的相片
            image = face_recognition.load_image_file(image_path)

            # Find all facial features in all the faces in the image
            face_landmarks_list = face_recognition.face_landmarks(image)

            pil_image = Image.fromarray(image)
            for face_landmarks in face_landmarks_list:
                d = ImageDraw.Draw(pil_image, 'RGBA')

                # 為嘴唇上色
                d.polygon(face_landmarks['top_lip'], fill=(r, g, b, 128))
                d.polygon(face_landmarks['bottom_lip'], fill=(r, g, b, 128))
                d.line(face_landmarks['top_lip'], fill=(r, g, b, 64), width=4)
                d.line(face_landmarks['bottom_lip'], fill=(r, g, b, 64), width=4)

            # 將上完口紅的照片存到lip的資料夾
            #full_filename = os.path.join(app.config['RESULT_LIP'], color + "_" + image_path.filename)
            #pil_image.save(full_filename)

            # pil_image.show()

            # --------------------------------------------------------------------------------------

            # 取得眉毛的編號
            eyebrow = request.form['shape']

            # 取得使用者所選眉毛的檔案名稱
            full_filenameL = os.path.join(app.config["EYEBROW"], eyebrow + "-l.png")
            full_filenameR = os.path.join(app.config["EYEBROW"], eyebrow + "-r.png")

            leftL = 0
            rightL = 0
            topL = 0
            bottomL = 0

            leftR = 0
            rightR = 0
            topR = 0
            bottomR = 0

            #pil_image = Image.fromarray(image)

            for face_landmarks in face_landmarks_list:

                first = True

                # 抓到左邊眉毛最小的長方形
                for point in face_landmarks['left_eyebrow']:
                    if first:
                        leftL = point[0]
                        rightL = point[0]
                        topL = point[1]
                        bottomL = point[1]
                        first = False
                    else:
                        if point[0] < leftL:
                            leftL = point[0]
                        if point[0] > rightL:
                            rightL = point[0]
                        if point[1] < topL:
                            topL = point[1]
                        if point[1] > bottomL:
                            bottomL = point[1]

                first = True
                # 抓到右邊眉毛最小的長方形
                for point in face_landmarks['right_eyebrow']:
                    if first:
                        leftR = point[0]
                        rightR = point[0]
                        topR = point[1]
                        bottomR = point[1]
                        first = False
                    else:
                        if point[0] < leftR:
                            leftR = point[0]
                        if point[0] > rightR:
                            rightR = point[0]
                        if point[1] < topR:
                            topR = point[1]
                        if point[1] > bottomR:
                            bottomR = point[1]

                print("臉部特徵({})座標點：{}".format('left_eyebrow', face_landmarks['left_eyebrow']))

                # 左眉毛由上往下圖上皮膚的顏色
                for y in range(topL - 5, bottomL + 2):
                    # for y in range(bottomL+2, topL-5, -1): 由下往上
                    for x in range(leftL + 3, rightL + 2):
                        pil_image.putpixel((x, y), pil_image.getpixel((x, y - 1)))

                # 右眉毛由上往下圖上皮膚的顏色
                for y in range(topR - 5, bottomR + 2):
                    for x in range(leftR + 3, rightR + 2):
                        pil_image.putpixel((x, y), pil_image.getpixel((x, y - 1)))

            # 開啟照片
            img = pil_image
            img = img.convert('RGBA')

            # 開啟左眉毛
            eyebrowL = Image.open(full_filenameL)
            eyebrowL = eyebrowL.convert('RGBA')

            # 開啟右眉毛
            eyebrowR = Image.open(full_filenameR)
            eyebrowR = eyebrowR.convert('RGBA')

            # 新建一個透明的底圖
            resultPicture = Image.new('RGBA', img.size, (0, 0, 0, 0))
            # 把照片貼到底圖
            resultPicture.paste(img, (0, 0))

            for face_landmarks in face_landmarks_list:

                first = True

                # 抓到左邊眉毛最小的長方形
                for point in face_landmarks['left_eyebrow']:
                    if first:
                        leftL = point[0]
                        rightL = point[0]
                        topL = point[1]
                        bottomL = point[1]
                        first = False
                    else:
                        if point[0] < leftL:
                            leftL = point[0]
                        if point[0] > rightL:
                            rightL = point[0]
                        if point[1] < topL:
                            topL = point[1]
                        if point[1] > bottomL:
                            bottomL = point[1]

                first = True
                # 抓到右邊眉毛最小的長方形
                for point in face_landmarks['right_eyebrow']:
                    if first:
                        leftR = point[0]
                        rightR = point[0]
                        topR = point[1]
                        bottomR = point[1]
                        first = False
                    else:
                        if point[0] < leftR:
                            leftR = point[0]
                        if point[0] > rightR:
                            rightR = point[0]
                        if point[1] < topR:
                            topR = point[1]
                        if point[1] > bottomR:
                            bottomR = point[1]

                # 重設的寬為原眉毛的寬度
                newWidthL = rightL - leftL
                # 重設的高依據原眉毛的高
                newHeightL = bottomL - topL
                # 重設左眉毛圖片
                imageL_resize = eyebrowL.resize((newWidthL, newHeightL))

                # 重設的寬為原眉毛的寬度
                newWidthR = rightR - leftR
                # 重設的高依據原眉毛的高
                newHeightR = bottomR - topR
                # 重設右眉毛圖片
                imageR_resize = eyebrowR.resize((newWidthR, newHeightR))

                # 設定簽名檔的位置參數
                right_bottomL = (leftL, topL)
                right_bottomR = (leftR, topR)

                # 為了背景保留透明度，將im參數與mask參數皆帶入重設過後的簽名檔圖片
                resultPicture.paste(imageL_resize, right_bottomL, imageL_resize)
                resultPicture.paste(imageR_resize, right_bottomR, imageR_resize)

            # resultPicture.show()

            # 將置換完眉毛的照片以.png檔存到eyebrow的資料夾中
            length = len(image_path.filename)
            filename = color+"_"+request.form['shape'] + "_" + image_path.filename[0:length - 4] + ".png"
            full_filename = os.path.join(app.config['RESULT_MIX'], filename)
            resultPicture.save(full_filename)

            # 將圖片的檔名傳給return給網頁
            return render_template("forms/uploadImage_result.html", image=full_filename1, result=full_filename)
