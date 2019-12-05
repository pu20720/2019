nnet = load('net10.15.mat') ;
parrotObj = parrot;
cameraObj = camera(parrotObj,'FPV');


%while(parrotObj.BatteryLevel>20)
while(1)
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
    catch e
        disp(e.identifier);
        disp(e.message);
    end

end
