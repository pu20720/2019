import uuid
from bluetooth import *
#import bluetooth
import RPi.GPIO as GPIO
import time
from time import sleep
#LED_PIN = 18
import sys
import subprocess
from picamera import PiCamera

def main():
        global Motor_L1_Pin
        global Motor_L2_Pin
        global Motor_R1_Pin
        global Motor_R2_Pin
        global camera
        camera = PiCamera()
        Motor_L1_Pin = 17
        Motor_L2_Pin = 18
        Motor_R1_Pin = 20
        Motor_R2_Pin = 21
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(Motor_R1_Pin, GPIO.OUT)
        GPIO.setup(Motor_R2_Pin, GPIO.OUT)
        GPIO.setup(Motor_L1_Pin, GPIO.OUT)
        GPIO.setup(Motor_L2_Pin, GPIO.OUT)
        #server_socket=BluetoothSocket(RFCOMM)
        server_socket=BluetoothSocket(RFCOMM)
        server_socket.bind(("", PORT_ANY))
        server_socket.listen(1)
        port = server_socket.getsockname()[1]
        service_id = str(uuid.uuid4())
        advertise_service(server_socket, "LEDServer",service_id = service_id,service_classes = [service_id, SERIAL_PORT_CLASS],profiles = [SERIAL_PORT_PROFILE])
        global client_socket
        try:
                print("按下 Ctrl-C 可停止程式")
                while True:
                        print('等待 RFCOMM 頻道 {} 的連線'.format(port))
                        client_socket, client_info = server_socket.accept()
                        print('接受來自 {} 的連線'.format(client_info))
                        try:
                                while True:
                                        movement = client_socket.recv(1024).decode().lower()
                                        if len(movement) == 0:
                                                break
                                        else:
                                                car_move(movement)
                        except IOError:
                                pass
                        client_socket.close()
                        print('中斷連線')
        except KeyboardInterrupt:
                GPIO.output(Motor_L1_Pin, GPIO.LOW)
                GPIO.output(Motor_L2_Pin, GPIO.LOW)
                GPIO.output(Motor_R1_Pin, GPIO.LOW)
                GPIO.output(Motor_R2_Pin, GPIO.LOW)
                print('中斷程式')
        finally:
            if 'client_socket' in vars():
                client_socket.close()
            server_socket.close()
            GPIO.cleanup()
            print('中斷連線')

        def camera_stream():
            print('take pic')
            camera.resolution = (1024, 768)
            camera.start_preview()
            camera.capture('/home/pi/darknet-nnpack/data/trash_image.png')
            camera.stop_preview()

        def enable_object_detection():
            camera_stream()
            print('detecting')
            result = subprocess.run(
                ['./darknet', 'detect', 'cfg/yolov2-tiny.cfg', 'yolov2-tiny.weights', 'data/trash_image.png'],
                stdout=subprocess.PIPE)
            v_result = result.stdout.decode('utf-8')
            print(v_result)
            p_result = process_result_to_car_move(v_result)
            return p_result

        def process_result_to_car_move(v_result):
            count_nextline = 0
            for i in range(len(v_result)):
                if v_result[i] == '\n':
                    count_nextline += 1
                    if count_nextline >= 1 and (i+3) < len(v_result) and v_result[i] == '\n':
                        print(v_result[i+1:i+6]) == 'pizza'
                        if v_result[i+1:i+6] == 'pizza':
                            GPIO.output(Motor_L1_Pin, GPIO.LOW)
                            GPIO.output(Motor_L2_Pin, GPIO.HIGH)
                            GPIO.output(Motor_R1_Pin, GPIO.LOW)
                            GPIO.output(Motor_R2_Pin, GPIO.HIGH)
                        elif v_result[i+1:i+5] == 'stop':
                            return v_result[i+1:i+5]

        def car_move(movement):
            p_move = ''
            print('do func car_move')
            if movement == 'search':
                print(movement)
                while (p_move != 'stop'):

                    GPIO.output(Motor_L1_Pin, GPIO.HIGH)
                    GPIO.output(Motor_L2_Pin, GPIO.LOW)
                    GPIO.output(Motor_R1_Pin, GPIO.LOW)
                    GPIO.output(Motor_R2_Pin, GPIO.HIGH)
                    p_move = enable_object_detection()
                car_move('stop')

            elif movement == 'up':
                print(movement)
                GPIO.output(Motor_L1_Pin, GPIO.LOW)
                GPIO.output(Motor_L2_Pin, GPIO.HIGH)
                GPIO.output(Motor_R1_Pin, GPIO.LOW)
                GPIO.output(Motor_R2_Pin, GPIO.HIGH)
            elif movement == 'right':
                print(movement)
                GPIO.output(Motor_L1_Pin, GPIO.HIGH)
                GPIO.output(Motor_L2_Pin, GPIO.LOW)
                GPIO.output(Motor_R1_Pin, GPIO.LOW)
                GPIO.output(Motor_R2_Pin, GPIO.LOW)
            elif movement == 'left':
                print(movement)
                GPIO.output(Motor_L1_Pin, GPIO.LOW)
                GPIO.output(Motor_L2_Pin, GPIO.LOW)
                GPIO.output(Motor_R1_Pin, GPIO.HIGH)
                GPIO.output(Motor_R2_Pin, GPIO.LOW)
            elif movement == 'stop':
                print(movement)
                GPIO.output(Motor_L1_Pin, GPIO.LOW)
                GPIO.output(Motor_L2_Pin, GPIO.LOW)
                GPIO.output(Motor_R1_Pin, GPIO.LOW)
                GPIO.output(Motor_R2_Pin, GPIO.LOW)
            else:
                print('未知的指令: {}'.format(data))

        if _name_ == '_main_':
            main()