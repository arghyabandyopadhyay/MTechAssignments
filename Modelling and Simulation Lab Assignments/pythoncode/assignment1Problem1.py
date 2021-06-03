from shapely.geometry import LineString
from matplotlib import pyplot as plt

if __name__ == '__main__':
    x1 = [450, 0]
    y1 = [0, 450]
    plt.plot(x1, y1)
    x2 = [300, 0]
    y2 = [0, 600]
    plt.plot(x2, y2)
    plt.xlabel('No of production of A')
    plt.ylabel('No of production of B')
    plt.title('Plot for the problem 1')
    line1 = LineString([(450, 0), (0, 450)])
    line2 = LineString([(300, 0), (0, 600)])
    intersection = line1.intersection(line2)
    plt.plot(*intersection.xy, 'go')
    plt.plot(0, 450, 'b*')
    plt.plot(300, 0, 'r*')
    p1, q1 = intersection.xy
    x = []
    y = []
    x.append(0)
    x.append(round(p1[0]))
    x.append(300)
    y.append(450)
    y.append(round(q1[0]))
    y.append(0)
    plt.fill_between(x, y, color='blue', alpha=0.2)
    plt.text(260, 200, "x + y = 450")
    plt.text(55, 500, "2x + y = 600")
    plt.show()
    print("Point of Intersection 1: ")
    print(p1[0])
    print(q1[0])
    z = []
    for i in range(len(x)):
        eqn = 3 * x[i] + 4 * y[i]
        z.append(eqn)
        print("Z = ", z)
    max_val = max(z)
    xy_index = z.index(max_val)
    print("The Value of Z ", max_val, " at point (", x[xy_index], ",", y[xy_index], ")"
          )