import pyqrcode as QRCODE

qrcode_enlace = QRCODE.create("https://www.youtube.com/watch?v=mlt1t5zDtbs&ab_channel=codigofacilito")
qrcode_enlace.png("pruebaQR2.png")

