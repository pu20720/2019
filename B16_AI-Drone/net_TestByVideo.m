nnet = load('net10.15.mat') ;
videoObj = VideoReader('Bebop_2_2019-10-14T165157+0800_E2767A.mp4');
numFrames = videoObj.NumberOfFrames;

for k = 1:numFrames
    picture = read(videoObj,k);

    
    try
        resizedPicture = imresize(picture,[227,227]);  % Resize the picture
        tic;

        label = classify(nnet.net,resizedPicture);
        
        fprintf('%f %s \n',toc,char(label));
        imshow(picture);                               % Show the picture
        title(char(label));                            % Show the label
        drawnow;
    catch e
        disp(e.identifier);
        disp(e.message);
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
