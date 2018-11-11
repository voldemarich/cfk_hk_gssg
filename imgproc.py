import numpy as np
import cv2 as cv
import matplotlib.pyplot as plt
from os import path
from os import listdir

import time
start_time = time.time()

pot_cascade = cv.CascadeClassifier('cascades/cascade6.xml')
imgpath = "/media/voldemarich/lower_tier_storage/tk/data1/"
savepath = "/media/voldemarich/lower_tier_storage/tk/saves/"

count = 0
detects = 0
for i in listdir(imgpath):
    thispath = path.join(imgpath,i)
    try:
        img = cv.imread(thispath)
        gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    except:
        continue

    pots = pot_cascade.detectMultiScale(img)
    count+=1
    if len(pots) > 0:
        detects+=1
    if count % 100 == 0:
        nowtime = time.time() - start_time
        print "Done {} records, of those detected {}, time from start is {}, which is {}s/img".format(count, detects, nowtime, nowtime/count)
    if len(pots) > 0:
        for (x, y, w, h) in pots:
            if w<250 or h<250:
                continue
            img = img[y:y+h, x:x+w]
            cv.imwrite(path.join(savepath,i), img)
            break
    #     plt.imshow(img)
    #     plt.show()
    #     break
