import pandas as pd
import matplotlib.pyplot as plt
import os

def plot_group(df, title, algorithms, filename):
    """Helper function to plot a group of algorithms."""
    plt.style.use('seaborn-v0_8-whitegrid')
    fig, ax = plt.subplots(figsize=(10, 6))

    for algo in algorithms:
        subset = df[df['algorithm_name'] == algo]
        if not subset.empty:
            ax.plot(subset['input_size'], subset['time_ns'], marker='o', linestyle='-', label=algo)

    ax.set_title(title, fontsize=16)
    ax.set_xlabel('Input Size (N)', fontsize=12)
    ax.set_ylabel('Average Time (nanoseconds)', fontsize=12)
    ax.legend()
    ax.grid(True)
    
    # Use scientific notation for y-axis if numbers are large
    ax.ticklabel_format(style='sci', axis='y', scilimits=(0,0))

    plt.tight_layout()
    plt.savefig(filename)
    plt.close()
    print(f"Generated {filename}")

def main():
    """Main function to read CSV and generate all plots."""
    csv_path = 'algorithm_runs.csv'
    if not os.path.exists(csv_path):
        print(f"Error: {csv_path} not found. Run the benchmark first.")
        return

    df = pd.read_csv(csv_path)

    # Plot 1: Search Algorithms
    search_algos = ['Linear Search', 'Binary Search']
    plot_group(df, 'Performance of Search Algorithms', search_algos, 'search_performance.png')

    # Plot 2: Sorting Algorithms (O(n^2))
    quadratic_sort_algos = ['Selection Sort', 'Insertion Sort']
    plot_group(df, 'Performance of O(n^2) Sorting Algorithms', quadratic_sort_algos, 'sorting_performance_quadratic.png')

    # Plot 3: Sorting Algorithms (O(n log n))
    loglinear_sort_algos = ['Merge Sort', 'Quicksort']
    plot_group(df, 'Performance of O(n log n) Sorting Algorithms', loglinear_sort_algos, 'sorting_performance_loglinear.png')

    # Plot 4: Tree Insertion
    tree_algos = ['BST Insert', 'Red-Black Tree Insert']
    plot_group(df, 'Performance of Tree Insertion', tree_algos, 'tree_insertion_performance.png')

    # Plot 5: Hash Table Insertion
    hash_algos = ['HashTable Insert (LF~0.7)']
    plot_group(df, 'Performance of Hash Table Insertion', hash_algos, 'hashtable_performance.png')

    # Plot 6: Heap Operations
    heap_algos = ['MinHeap Insert', 'MinHeap Extract']
    plot_group(df, 'Performance of Heap Operations', heap_algos, 'heap_performance.png')

    # Plot 7: Graph Algorithms
    graph_algos = ['BFS', 'DFS', 'Dijkstra', 'Kruskal MST', 'Prim MST']
    plot_group(df, 'Performance of Graph Algorithms', graph_algos, 'graph_performance.png')

if __name__ == '__main__':
    main()