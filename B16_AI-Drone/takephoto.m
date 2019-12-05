parrotObj = parrot;
cameraObj = camera(parrotObj,'FPV');
counter = 1;
while(1)
    inpKey = '';
    input(inpKey);
    if(isempty(inpKey))
        if(strcmp(inpKey,'q'))
            break;
        else
            picture = snapshot(cameraObj);
            imshow(picture);
            filename = [int2str(counter),'.jpg'];
            counter = counter + 1;
            imwrite(picture,filename)
            result = 0;
        end
    end
end