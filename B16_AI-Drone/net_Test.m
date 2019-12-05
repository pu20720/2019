%nnet = googlenet;
%cameraObj = VideoReader('VID165625.mp4');
nnet = load('net10.15.mat') ;
parrotObj = parrot;
cameraObj = camera(parrotObj,'FPV');


while(parrotObj.BatteryLevel>20)
    picture = snapshot(cameraObj);
    
    try
        resizedPicture = imresize(picture,[227,227]);  % Resize the picture
        tic;
        label = classify(nnet.net,resizedPicture);
        label = char(label);
        fprintf('%f %s \n',toc,label);
        imshow(picture);                               % Show the picture
        title(char(label));                            % Show the label
        drawnow;
    catch ErrorInfo
        disp(ErrorInfo);
    end

end

%{
numFrames = cameraObj.NumberOfFrames;
for x = 1:numFrames
    picture = read(cameraObj,x);
    resizedPicture = imresize(picture,[224,224]);  % Resize the picture
    tic;
    label = classify(nnet.net,resizedPicture,'ExecutionEnvironment','gpu');
    disp(toc);
    %disp(label);
    imshow(picture);                               % Show the picture
    title(char(label));                            % Show the label
    drawnow;
end
%}
