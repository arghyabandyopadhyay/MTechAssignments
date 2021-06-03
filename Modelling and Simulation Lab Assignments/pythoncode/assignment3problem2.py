import numpy as np


def check_loop(p, row, column):
    p[row, column] = -1
    flag = 1
    while flag != 0:
        flag = 0
        if p.size != 0:
            row = np.count_nonzero(p, axis=1)
            f = 0
            for index in range(len(row)):
                if row[index] < 2:
                    flag = 1
                    p = np.delete(p, (index - f), axis=0)
                    f += 1
        if p.size != 0:
            e = 0
            col = np.count_nonzero(p, axis=0)
            for index in range(len(col)):
                if col[index] < 2:
                    flag = 1
                    p = np.delete(p, (index - e), axis=1)
                    e += 1
    if p.size != 0:
        return 0
    else:
        return 1


if __name__ == '__main__':
    cm = np.array([
        [4.0, 3.0, 1.0, 2.0, 6.0],
        [5.0, 2.0, 3.0, 4.0, 5.0],
        [3.0, 5.0, 6.0, 3.0, 2.0],
        [2.0, 4.0, 4.0, 5.0, 3.0]])
    s = np.array([80.0, 40.0, 40.0, 20.0])
    d = np.array([60.0, 60.0, 30.0, 40.0, 10.0])
    c = cm.copy()
    print("The Cost Matrix is: ")
    print(c)
    print("The Supply is: ", s)
    print("The Demand is: ", d)
    m, n = c.shape
    print("No of Rows & No of Columns: (", m, ", ", n, ")")
    total_cost = 0
    no_alloc = 0
    total_demand = np.sum(d)
    total_supply = np.sum(s)
    alloc = []
    if total_demand == total_supply:
        print("It is a Balanced Transportation Problem")
    else:
        print("It is an UnBalanced Transportation Problem")
        if total_demand > total_supply:
            new = np.array(np.zeros(n))
            c = np.row_stack((c, new))
            s = np.append(s, total_demand - total_supply)
            m = m + 1
        else:
            new = np.array(np.zeros(m))
            c = np.column_stack((c, new))
            d = np.append(d, total_supply - total_demand)
            n = n + 1
        print("The New Balanced Cost Matrix is: ")
        print(c)
        print("The Supply is: ", s)
        print("The Demand is: ", d)
    a = np.zeros(c.shape)
    min_cost = np.amin(c)
    while min_cost != np.inf:
        indexes = np.where(c == min_cost)
        i = indexes[0][0]
        j = indexes[1][0]
        x = min(s[i], d[j])
        s[i] -= x
        d[j] -= x
        total_cost += (x * c[i, j])
        no_alloc += 1
        a[i, j] = x
        alloc.append((i, j))
        if s[i] < d[j]:
            x = 0
            while x < n:
                c[i, x] = np.inf
                x += 1
        elif s[i] > d[j]:
            y = 0
            while y < m:
                c[y, j] = np.inf
                y += 1
        else:
            x = 0
            while x < n:
                c[i, x] = np.inf
                x += 1
            y = 0
            while y < m:
                c[y, j] = np.inf
                y += 1
        min_cost = np.amin(c)
    print("Total Cost: ", total_cost)
    unalloc = []
    for i in range(m):
        for j in range(n):
            if not (i, j) in alloc:
                unalloc.append((i, j))
    print("List of Allocated Positions: ", alloc)
    print("List of Unallocated Positions: ", unalloc)
    print("Allocation Matrix: ")
    print(a)
    no_loop = []
    if no_alloc == m + n - 1:
        print("Non Degeneracy")
    else:
        print("Degeneracy")

        for i in unalloc:
            if check_loop(a.copy(), i[0], i[1]) == 1:
                no_loop.append(i)

        min_epi_list = []
        for i in no_loop:
            min_epi_list.append(cm[i[0], i[1]])

        min_epi = min(min_epi_list)
        ind = min_epi_list.index(min_epi)
        loc = no_loop[ind]
        a[loc[0], loc[1]] = -1
        print("Allocation Matrix After Converting Degeneracy to Non-Degeneracy is : ")
        print(a)