import cv2
import numpy as np
import matplotlib.pyplot as plt
from os import path
from os import listdir

import json
import time
from multiprocessing import Process


def estimateCoffee(image, estimation_rounds=1):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    kernel = np.ones((3, 3), np.float32) / 9
    dst = cv2.filter2D(image, -1, kernel)
    #plt.figure(), plt.imshow(image)
    imh,imw = image.shape
    image = image[int(0.14*imh):imh, 0:imw]
    image = cv2.addWeighted(image, 1.1, image, 0, -3)
    imh,imw = image.shape
    search = 0
    nlines = estimation_rounds
    localimage = image[search:imh, 0:imw]
    h, w = localimage.shape
    localsearch = 0
    diff = 0
    for i in range(1,h):
        roi1 = localimage[0:i,0:w]
        roi2 = localimage[i:h, 0:w]
        localdiff = roi2.mean() - roi1.mean()
        if localdiff<diff:
            diff = localdiff
            localsearch = i
    if diff > -35:
        return 0
    nlines-=1
    search+=localsearch
    # image = cv2.rectangle(image, (0,search), (imw,search), (255,0,0), 2)
    # plt.figure(), plt.imshow(image, 'gray')
    # plt.show()
    return round(1 - float(search)/imh, 2)*100



imgpath = "/media/voldemarich/lower_tier_storage/tk/saves/"



def mp_task(procnum, imgpath, imgcollection, jsonpath):
    print "Started process #{}".format(procnum)
    count = 0
    empty = 0
    required = 3000
    start_time = time.time()
    final_json = []
    for i in imgcollection:
        if count > required:
            print "Done."
            break
        thispath = path.join(imgpath,i)
        try:
            img = cv2.imread(thispath)
        except:
            continue

        coffe_estimate = estimateCoffee(img)
        final_json.append({"filename":i, "level":coffe_estimate})
        if coffe_estimate == 0:
            empty+=1
        count += 1
        if count % 100 == 0:
            nowtime = time.time() - start_time
            print "\rDone {} records, of those empty {}, time from start is {}, which is {}s/img".format(count, empty,
                                                                                                          nowtime,
                                                                                                          nowtime / count)

    fl = open(jsonpath, "w")
    json.dump(final_json, fl)
    fl.close()

def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in xrange(0, len(l), n):
        yield l[i:i + n]


processes = 8
col = listdir(imgpath)
chunksize = len(col)/processes
col_chunked = chunks(col, chunksize)
count = 1
processes = []
for portion in col_chunked:
    a = Process(target=mp_task, args=(count, imgpath, portion, "gath_data_{}.json".format(count)))
    processes.append(a)
    a.start()
    count += 1

while True:
    m = raw_input()
    if m == "terminate":
        for process in processes:
            process.stop()
    else:
        continue
