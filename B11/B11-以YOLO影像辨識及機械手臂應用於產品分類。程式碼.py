import math
import socket

#dType.SetHOMECmdEx(api, 0, 1) #歸零
dType.SetPTPCmdEx(api, 7, 0,  0,  40, 0, 1) #往上40
dType.SetEndEffectorParamsEx(api, 59.7, 0, 0, 1) #吸盤
dType.SetPTPCommonParamsEx(api,10000,10000,1)
dType.SetPTPJumpParamsEx(api,40,100,1) #門型
dType.SetInfraredSensor(api, 1 ,1, 0) #感測器

blueboxcount = 0
redboxcount = 0
mentholatumcount = 0
herbalcandyboxcount = 0
elsecount = 0
times = 20
x = 0
t = dType.gettime()[0]
while not False:
 if(dType.GetInfraredSensor(api, 1)[0]) == 0:
  if x == 1:
   t = dType.gettime()[0]
  x = 0
  dType.SetEMotorEx(api, 0, 1, (-20000), 1)
  if((dType.gettime()[0]) > (t+10)):
   dType.SetEMotorEx(api, 0, 0, 0, 1)
   break
 elif(dType.GetInfraredSensor(api, 1)[0]) == 1:
  x = 1
  dType.SetEMotorEx(api, 0, 0, 0, 1)
  for i in range(times):
   sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   host = "172.20.10.3"
   port = 13000
   sock.connect((host, port))
   data = sock.recv(1024)
   print(data.decode())
   sock.close() 
  if(data.decode() == 'BlueBox'): 
   current_pose = dType.GetPose(api)
   dType.SetPTPCmdEx(api, 2, 263.8959,  (-8.2933),  5.8486, current_pose[3], 1)
   dType.SetEndEffectorSuctionCupEx(api, 1, 1)
   dType.SetPTPCmdEx(api, 0, 151.9135,  (-250.7520),  (-50.3987 + blueboxcount), 0, 1)
   dType.SetEndEffectorSuctionCupEx(api, 0, 1)
   dType.SetPTPCmdEx(api, 0, 257.2624,  (-8.7987),  45.6169, 0, 1)
   blueboxcount = blueboxcount + 22
  elif(data.decode() == 'RedBox'):
   current_pose = dType.GetPose(api)
   dType.SetPTPCmdEx(api, 2, 257.2624,  (-8.7987),  5.6169, current_pose[3], 1)
   dType.SetEndEffectorSuctionCupEx(api, 1, 1)
   dType.SetPTPCmdEx(api, 0, 147.8491,  (-182.6360),  (-48.0458 + redboxcount), 0, 1)
   dType.SetEndEffectorSuctionCupEx(api, 0, 1)
   dType.SetPTPCmdEx(api, 0, 257.2624,  (-8.7987),  45.6169, 0, 1)
   redboxcount = redboxcount + 22
  elif(data.decode() == 'Mentholatum'):
   current_pose = dType.GetPose(api)
   dType.SetPTPCmdEx(api, 2, 262.1769,  (-16.7380),  (-2.2199), current_pose[3], 1)
   dType.SetEndEffectorSuctionCupEx(api, 1, 1)
   dType.SetPTPCmdEx(api, 0, 60.8841,  (-284.0700),  (-55.3838 + mentholatumcount), 0, 1)
   dType.SetEndEffectorSuctionCupEx(api, 0, 1)
   dType.SetPTPCmdEx(api, 0, 257.2624,  (-8.7987),  45.6169, 0, 1)
   mentholatumcount = mentholatumcount + 15
  elif(data.decode() == 'HerbalCandyBox'):
   current_pose = dType.GetPose(api)
   dType.SetPTPCmdEx(api, 2, 261.9617,  (-21.7132),  10.0396, current_pose[3], 1)
   dType.SetEndEffectorSuctionCupEx(api, 1, 1)
   dType.SetPTPCmdEx(api, 0, 50.4356,  (-208.8040),  (-42.8860 + herbalcandyboxcount), 0, 1)
   dType.SetEndEffectorSuctionCupEx(api, 0, 1)
   dType.SetPTPCmdEx(api, 0, 257.2624,  (-8.7987),  45.6169, 0, 1)
   herbalcandyboxcount = herbalcandyboxcount + 26.5
  else:
   current_pose = dType.GetPose(api)
   dType.SetPTPCmdEx(api, 2, 263.3571,  (-9.3726),  8.4365, current_pose[3], 1)
   dType.SetEndEffectorSuctionCupEx(api, 1, 1)
   dType.SetPTPCmdEx(api, 0, (-23.5540),  (-250.4098),  (-43.4482 + elsecount), 0, 1)
   dType.SetEndEffectorSuctionCupEx(api, 0, 1)
   dType.SetPTPCmdEx(api, 0, 257.2624,  (-8.7987),  45.6169, 0, 1)
   elsecount = elsecount + 25
  
 
  


