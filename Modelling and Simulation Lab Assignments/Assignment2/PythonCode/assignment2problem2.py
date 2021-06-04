import numpy as np

if __name__ == '__main__':
    total_cost = 0
    no_alloc = 0
    cm = np.array([[9, 12, 9, 6, 9, 10, 5], [7, 3, 7, 7, 5, 5, 6], [6, 5, 9, 11, 3, 11, 2], [6, 8, 11, 2, 2, 10, 9],
              [4, 4, 6, 2, 4, 2, 0]]
             )
    print("Cost Matrix")
    print(cm)
    # print()
    r, c = cm.shape
    print("Rows, Columns: (", r - 1, ",", c - 1, ")")
    # print()
    total_demand = np.sum(cm[r - 1, :])
    total_supply = np.sum(cm[:, c - 1])
    if total_demand == total_supply:
        print("Balanced Transportation Problem.")
    else:
        print("Unbalanced Transportation Problem")
    print()
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
        print("Total Cost: ", total_cost)
        # print()
        print("No of Allocation: ", no_alloc)
        # print()
        if ((r - 1) + (c - 1) - 1) == no_alloc \
                and total_demand == total_supply:
            print("Non Degenerate & Feasible Solution")
        else:
            print("Degenerate Solution")
