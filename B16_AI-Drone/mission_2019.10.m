function result = mission(droneObj)
    disp('IntoMission');
    cameraObj = camera(droneObj,'FPV');
    nnet = load('net10.15.mat') ;
    rnet = load('net_regression_10.16.mat') ;
    %begin mission
    fprintf('BattreyLevel = %d\n',droneObj.BatteryLevel);
    counterT = 0;
    unsafeCounter = 0;
    while(droneObj.BatteryLevel > 10)
        picture = snapshot(cameraObj);
        counterT = counterT + 1;
        try
            resizedPicture = imresize(picture,[227,227]);  % Resize the picture
            picture = snapshot(cameraObj);
            tic;
            
            label = classify(nnet.net,resizedPicture);
            Xcoordinates = predict(rnet.net_regression,resizedPicture);
            Xcoordinates = int16(Xcoordinates*856/1920);
            
            label = char(label);
            
            droneState = droneObj.State;
            droneState = char(droneState);
            
            disp(droneState);
            if(~strcmp(droneState,'flying') && ~strcmp(droneState,'hovering'))
                disp('Wrong mode, mission failed')
                break
            end
            
            if(strcmp(label,'safe'))
                degreeForward = 0.5 - counterT / 50;
                if(degreeForward <= 0.2)
                    degreeForward = 0.2;
                end
                moveforward(droneObj,0.1,deg2rad(degreeForward));
                disp(degreeForward);
                unsafeCounter = 0;
                
            elseif(strcmp(label,'unsafe'))  
                unsafeCounter = unsafeCounter + 1;
                if(unsafeCounter == 3)
                    %moveback(droneObj,0.1,deg2rad(0.7));
                    land(droneObj);
                    disp('Unsafe over 3 times')
                    counterT = 0;
                    break
                end
            else
                fprintf('Not valiable, Get %s\n',label);
            end
            
            if((Xcoordinates - 856/2) > 100)
                turn(droneObj,deg2rad(10));
                disp('Right Turn');
            elseif((Xcoordinates - 856/2) < -100)
                turn(droneObj,deg2rad(-10));
                disp('Left Turn');
            end
            
            fprintf('%f %s %s\n',toc,label,int2str(Xcoordinates));
            imshow(picture);                               % Show the picture
            title(char(label));                            % Show the label
            line([Xcoordinates Xcoordinates],[0 1080],'LineWidth',6)
            drawnow;
            
        catch ErrorInfo
            disp(ErrorInfo);
            
        end
    end    
    %end mission
    result = 0;