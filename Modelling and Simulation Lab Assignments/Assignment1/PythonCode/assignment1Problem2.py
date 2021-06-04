from shapely.geometry import LineString
from matplotlib import pyplot as plt

if __name__ == '__main__':
    x1 = [0,16.66]
    y1 = [12.5,0]
    plt.plot(x1, y1)
    x2 = [17.14,0]
    y2 = [0,10]
    plt.plot(x2, y2)
    plt.xlabel('Amount of Eggs in gms')
    plt.ylabel('Amount of Milk in gms')
    plt.title('Plot for the problem 2')
    line1 = LineString([(16.66,0),(0,12.5)])
    line2 = LineString([(17.14,0),(0,10)])
    intersection = line1.intersection(line2)
    plt.plot(*intersection.xy, 'go')
    plt.plot(0,12.5,'b*')
    plt.plot(17.14,0,'r*')
    p1, q1 = intersection.xy
    x = []
    y = []
    x.append(0)
    x.append(round(p1[0],2))
    x.append(17.14)
    y.append(12.5)
    y.append(round(q1[0],2))
    y.append(0)
    plt.fill_between(x,y,max(y),color='blue',alpha=0.2)
    plt.text(7,8,"6x + 8y = 100")
    plt.text(3,5,"7x + 12y = 120")
    plt.show()
    print("Point of Intersection 1: ")
    print(round(p1[0],2))
    print(round(q1[0],2))
    z=[]
    for i in range(len(x)):
        eqn = 12*x[i] + 20*y[i]
        z.append(round(eqn,2))
        print("Z = ",z)
    min_val = min(z)
    xy_index = z.index(min_val)
    print("The Value of Z ",min_val," at point (",x[xy_index],",",y[xy_index],")"
    )