import numpy as np

if __name__ == '__main__':
    total_cost = 0
    no_alloc = 0
    cm = np.array([[7.0, 6, 4, 5, 9],
                        [8, 5, 6, 7, 8],
                        [6, 8, 9, 6, 5],
                        [5, 7, 7, 8, 6]])
    s_main = np.array([40.0, 30, 20, 10])
    d_main = np.array([30.0, 30, 15, 20, 5])
    print("Cost Matrix")
    print(cm)

    r, c = cm.shape
    print("Rows, Columns: (", r - 1, ",", c - 1, ")")

    total_demand = np.sum(cm[r - 1, :])
    total_supply = np.sum(cm[:, c - 1])
    if total_demand == total_supply:
        print("Balanced Transportation Problem.")
    else:
        print("Unbalanced Transportation Problem")

    i = 0
    j = 0
    while (i < r - 1) and (j < c - 1):
        x = min(cm[r - 1, j], cm[i, c - 1])
        cm[r - 1, j] = cm[r - 1, j] - x
        cm[i, c - 1] = cm[i, c - 1] - x
        total_cost = total_cost + x * cm[i, j]
        no_alloc = no_alloc + 1
        if cm[r - 1, j] < cm[i, c - 1]:
            j = j + 1
        elif cm[r - 1, j] > cm[i, c - 1]:
            i = i + 1
        else:
            i = i + 1
            j = j + 1
    print("Total Cost: ", total_cost * 100)

    print("No of Allocation: ", no_alloc)

    if ((r - 1) + (c - 1) - 1) == no_alloc and \
            total_demand == total_supply:
        print("Non Degenerate & Feasible Solution")
    else:
        print("Degenerate Solution")
