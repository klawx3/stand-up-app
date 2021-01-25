
from tkinter import *
import tkinter.scrolledtext as scrolledtext
from tkinter import messagebox, filedialog
from pyzbar.pyzbar import decode, ZBarSymbol
import cv2
import pyautogui
import numpy as np
import threading
from PIL import Image, ImageTk, ImageDraw
import os


class App:
    def __init__(self, font_video=0):
        self.active_camera = False
        self.info = []
        self.codelist = []
        self.appName = 'QR Code Reader'
        self.ventana = Tk()
        self.ventana.title(self.appName)
        self.ventana['bg'] = 'black'
        self.font_video = font_video
        #self.label = Label(self.ventana, text=self.appName, font=15, bg='blue',
                           #fg='white').pack(side=TOP, fill=BOTH)


        self.display = scrolledtext.ScrolledText(self.ventana, width=86, background='snow3'
                                                 , height=4, padx=10, pady=100, font=('Arial', 10))
        self.display.pack(side=BOTTOM)

        self.canvas = Canvas(self.ventana, bg='black', width=640, height=0)
        self.canvas.pack()

        self.btnCamera = Button(self.ventana, text="INICIAR LECTURA POR CAMARA", width=90, bg='goldenrod2',
                                activebackground='red', command=self.active_cam)
        self.btnCamera.pack(side=LEFT)


        self.ventana.mainloop()


    def visor(self):
        ret, frame = self.get_frame()
        if ret:
            self.photo = ImageTk.PhotoImage(image=Image.fromarray(frame))
            self.canvas.create_image(0, 0, image=self.photo, anchor=NW)
            self.ventana.after(15, self.visor)

    def active_cam(self):
        if self.active_camera == False:
            self.active_camera = True
            self.VideoCaptura()
            self.visor()
        else:
            self.active_camera = False
            self.codelist = []
            self.btnCamera.configure(text="INICIAR LECTURA POR CAMARA")
            self.vid.release()
            self.canvas.delete('all')
            self.canvas.configure(height=0)

    def capta(self, frm):
        self.info = decode(frm)
        cv2.putText(frm, "Muestre el codigo delante de la camara para su lectura", (84, 37), cv2.FONT_HERSHEY_SIMPLEX,
                    0.5, (0, 255, 0), 2)

        if self.info != []:
            self.display.delete('1.0', END)

            for code in self.info:
                if code not in self.codelist:
                    self.codelist.append(code)
                    self.display.insert(END, (code[0].decode('utf-8')) + '\n')
                self.draw_rectangle(frm)


    def get_frame(self):
        if self.vid.isOpened():
            verif, frame = self.vid.read()
            if verif:
                self.btnCamera.configure(text="CERRAR CAMARA")
                self.capta(frame)

                return (verif, cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))
            else:
                messagebox.showwarning("CAMARA NO DISPONIBLE", """La cámara está siendo utilizada por otra aplicación.
                Cierrela e intentelo de nuevo.""")
                self.active_cam()
                return (verif, None)
        else:
            verif = False
            return (verif, None)

    def draw_rectangle(self, frm):
        codes = decode(frm)
        for code in codes:
            data = code.data.decode('ascii')
            x, y, w, h = code.rect.left, code.rect.top, \
                         code.rect.width, code.rect.height
            cv2.rectangle(frm, (x, y), (x + w, y + h), (255, 0, 0), 6)
            cv2.putText(frm, code.type, (x - 10, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 50, 255), 2)

    def VideoCaptura(self):
        self.vid = cv2.VideoCapture(self.font_video)
        if self.vid.isOpened():
            self.width = self.vid.get(cv2.CAP_PROP_FRAME_WIDTH)
            self.height = self.vid.get(cv2.CAP_PROP_FRAME_HEIGHT)
            self.canvas.configure(width=self.width, height=self.height)
        else:
            messagebox.showwarning("CAMARA NO DISPONIBLE", "El dispositivo no está activado o disponible")
            self.display.delete('1.0', END)
            self.active_camera = False

    def __del__(self):
        if self.active_camera == True:
            self.vid.release()


if __name__ == "__main__":
    App()