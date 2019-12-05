clear;
imds=imageDatastore('Classification_1008','IncludeSubfolders',true,'LabelSource','foldernames');
[imdsTrain,imdsValidation]=splitEachLabel(imds,0.7,'randomized');
augimdsTrain=augmentedImageDatastore([224 224],imdsTrain);
augimdsValidation=augmentedImageDatastore([224 224],imdsValidation);

layers=[
    imageInputLayer([224 224 3],'Name','input','Normalization','zerocenter')
    convolution2dLayer([3 3],64,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv1_1')
    reluLayer('Name','relu1_1')
    convolution2dLayer([3 3],64,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv1_2')
    reluLayer('Name','relu1_2')
    maxPooling2dLayer([2 2],'Stride',[2 2],'Padding',[0 0 0 0],'Name','pool1')
    convolution2dLayer([3 3],128,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv2_1')
    reluLayer('Name','relu2_1')
    convolution2dLayer([3 3],128,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv2_2')
    reluLayer('Name','relu2_2')
    maxPooling2dLayer([2 2],'Stride',[2 2],'Padding',[0 0 0 0],'Name','pool2')
    convolution2dLayer([3 3],256,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv3_1')
    reluLayer('Name','relu3_1')
    convolution2dLayer([3 3],256,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv3_2')
    reluLayer('Name','relu3_2')
    convolution2dLayer([3 3],256,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv3_3')
    reluLayer('Name','relu3_3')
    convolution2dLayer([3 3],256,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv3_4')
    reluLayer('Name','relu3_4')
    maxPooling2dLayer([2 2],'Stride',[2 2],'Padding',[0 0 0 0],'Name','pool3')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv4_1')
    reluLayer('Name','relu4_1')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv4_2')
    reluLayer('Name','relu4_2')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv4_3')
    reluLayer('Name','relu4_3')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv4_4')
    reluLayer('Name','relu4_4')
    maxPooling2dLayer([2 2],'Stride',[2 2],'Padding',[0 0 0 0],'Name','pool4')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv5_1')
    reluLayer('Name','relu5_1')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv5_2')
    reluLayer('Name','relu5_2')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv5_3')
    reluLayer('Name','relu5_3')
    convolution2dLayer([3 3],512,'Stride',[1 1],'Padding',[1 1 1 1],'Name','conv5_4')
    reluLayer('Name','relu5_4')
    maxPooling2dLayer([2 2],'Stride',[2 2],'Padding',[0 0 0 0],'Name','pool5')
    
    fullyConnectedLayer(4096,'Name','fc6')
    reluLayer('Name','relu6')
    dropoutLayer('Name','drop6')
    fullyConnectedLayer(4096,'Name','fc7')
    reluLayer('Name','relu7')
    dropoutLayer('Name','drop7')
    fullyConnectedLayer(2,'Name','fc8')
    softmaxLayer('Name','prob')
    classificationLayer('Name','output')
    ];

options=trainingOptions('sgdm', ...
    'MiniBatchSize',30, ...
    'Shuffle','never', ...
    'MaxEpochs',1000, ...
    'InitialLearnRate',1e-4, ...
    'ValidationData',augimdsValidation, ...
    'ValidationFrequency',3, ...
    'Verbose',false, ...
    'Plots','training-progress');
net=trainNetwork(augimdsTrain,layers,options);

% Classify Validation Images
[YPred,probs] = classify(net,augimdsValidation);
accuracy = mean(YPred==imdsValidation.Labels)

