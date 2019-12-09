function varargout = flightGUI(varargin)
% FLIGHTGUI MATLAB code for flightGUI.fig
%      FLIGHTGUI, by itself, creates a new FLIGHTGUI or raises the existing
%      singleton*.
%
%      H = FLIGHTGUI returns the handle to a new FLIGHTGUI or the handle to
%      the existing singleton*.
%
%      FLIGHTGUI('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in FLIGHTGUI.M with the given input arguments.
%
%      FLIGHTGUI('Property','Value',...) creates a new FLIGHTGUI or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before flightGUI_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to flightGUI_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help flightGUI

% Last Modified by GUIDE v2.5 23-Sep-2019 16:19:12

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @flightGUI_OpeningFcn, ...
                   'gui_OutputFcn',  @flightGUI_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
disp('varargin');              
disp(varargin);          
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before flightGUI is made visible.
function flightGUI_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to flightGUI (see VARARGIN)
handles.in1 = varargin{1};
% Choose default command line output for flightGUI
handles.output = hObject;
global t;
t = timer('TimerFcn',{@sj,handles},'Period',1,'ExecutionMode','fixedRate');
% Update handles structure
guidata(hObject, handles);

% UIWAIT makes flightGUI wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = flightGUI_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
disp(handles);

function sj(hObject, eventdata, handles)
handles = guihandles(gcf);
%set(handles.text_battery,'string',num2str(handles.in1.BatteryLevel));

% --- Executes on button press in but_takeoff.
function but_takeoff_Callback(hObject, eventdata, handles)
% hObject    handle to but_takeoff (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
takeoff(handles.in1);
%movedown(handles.in1,0.5,0.3);
movedown(handles.in1,0.5,0.2);
%global t
%start(t)

% --- Executes on button press in but_execute.
function but_execute_Callback(hObject, eventdata, handles)
% hObject    handle to but_execute (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
mission(handles.in1);
%mission_reg(handles.in1);


% --- Executes on button press in but_land.
function but_land_Callback(hObject, eventdata, handles)
% hObject    handle to but_land (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
disp("prepare to land");
land(handles.in1);
disp("LANDED");


% --- Executes during object creation, after setting all properties.
function text_speed_CreateFcn(hObject, eventdata, handles)
% hObject    handle to text_speed (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% --- Executes during object creation, after setting all properties.
%function text_battery_CreateFcn(hObject, eventdata, handles)
% hObject    handle to text_battery (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called
