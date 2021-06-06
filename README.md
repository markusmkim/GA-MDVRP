# Using a genetic algorithm on the Multi-Depot Vehicle Routing Problem
The Multi-Depot Vehicle Routing Problem (MDVRP) is a classic example of a NP-Hard combinatorial optimization problem.
Here, a genetic algorithm (GA) is implemented and applied to 23 benchmark problems to find approximate solutions in polynomial time.
The implementation of the GA, in particular the evolutionary operators, is inspired by the work of Ombuki-Berman and Hanshar [1].

The rules of an MDVRP can be formulated as follows:
- A set of geographically dispersed customers with known demands are to be
  serviced by a fleet of vehicles. Each customer is to be fully serviced exactly once.
- Several vehicles are assigned to each depot. A vehicle must start from and return to the same depot.
- Each vehicle has a limited service capacity *Q*. That is, for a given vehicle the total customer demand on its route cannot exceed Q.
- For some problems there is also a maximum distance *R* for each single route.
- The objective is to maximize or minimize some goal. Here, **the objective is to minimize the total distance travelled by all vehicles
  across the depots to serve all customers**
  

## Results
The algorithm was tested on Cordeau's 23 benchmark instances [2].

#### Examples
The table below presents the solutions found on 3 of the instances in a graphical format.

Instance | p01 | p08 | p10
:-: | :-: | :-: | :-:
Solution | ![p01](/data/solutionImages/p01.png) | ![p02](/data/solutionImages/p08.png) | ![p03](/data/solutionImages/p10.png)
**Distance** | **597.66** | **4692.56** | **3936.96**
Depots | 4 | 2 | 4
Customers | 50 | 249 | 249
*Q* | 80 | 500 | 500
*R* | - | 310 | 310

#### All results
Then the solutions found for all 23 instances are presented in the table below.
A population size of 100 was used, and the algorithm was stopped after **reaching 1200 generations**.
For the smallest instances the algorithm reached 1200 generations in a matter of seconds, whereas on the bigger instances the algorithm 
(run on my PC with an i5-8250U CPU installed) had to spend up to 20 minutes.
The algorithm was run on each instance 10 times.
Both the distance of the best run (GA-best) and the average distance
of all 10 runs (GA-avg) are presented. The best known solutions (BKB) [3] are included as reference.
The error column shows the relative difference between BKB and GA-best (with BKB as reference point), while the deviation column presents the relative
difference between GA-avg with GA-best (with GA-avg as the reference point).

Instance | Customers | Depots | *Q* | *R* | BKB | GA-best | GA-avg | Error % | Deviation %
:-: | :-: | :-: | :-: | :-: | --: | --: | --: | :-: | :-:
p01 | 50 | 4 | 80 | - | **576.87** | **597.66** | **597.78** | 3.6 | 0.02
p02 | 50 | 4 | 160 | - | **473.53** | **490.50** | **493.62** | 3.58 | 0.63
p03 | 75 | 5 | 140 | - | **641.19** | **669.67** | **670.77** | 4.44 | 0.16
p04 | 100 | 2 | 100 | - | **1001.59** | **1055.34** | **1087.58** | 5.37 | 2.69
p05 | 100 | 2 | 200 | - | **750.03** | **774.74** | **776.97** | 3.29 | 0.29
p06 | 100 | 3 | 100 | - | **867.50** | **906.79** | **918.91** | 4.53 | 1.32
p07 | 100 | 4 | 100 | - | **885.80** | **929.83** | **935.55** | 4.97 | 0.61
p08 | 249 | 2 | 500 | 310 | **4420.94** | **4692.56** | **4751.61** | 6.14 | 1.24
p09 | 249 | 3 | 500 | 310 | **3900.22** | **4134.74** | **4139.20** | 6.01 | 0.11
p10 | 249 | 4 | 500 | 310 | **3663.02** | **3936.96** | **3943.01** | 7.48 | 0.15
p11 | 249 | 5 | 500 | 310 | **3554.18** | **3791.47** | **3948.86** | 6.68 | 3.99
p12 | 80 | 2 | 60 | - | **1318.95** | **1318.95** | **1324.58** | 0 | 0.43
p13 | 80 | 2 | 60 | 200 | **1318.95** | **1318.95** | **1325.40** | 0 | 0.49
p14 | 80 | 2 | 60 | 180 | **1360.12** | **1365.69** | **1365.69** | 0.41 | 0
p15 | 160 | 4 | 60 | - | **2505.42** | **2624.12** | **2630.59** | 4.74 | 0.25
p16 | 160 | 4 | 60 | 200 | **2572.23** | **2629.82** | **2638.80** | 2.24 | 0.34
p17 | 160 | 4 | 60 | 180 | **2709.09** | **2731.37** | **2731.37** | 0.82 | 0
p18 | 240 | 6 | 60 | - | **3702.85** | **4022.51** | **4036.07** | 8.63 | 0.34
p19 | 240 | 6 | 60 | 200 | **3827.06** | **3959.78** | **3973.95** | 3.47 | 0.36
p20 | 240 | 6 | 60 | 180 | **4058.07** | **4097.06** | **4097.06** | 0.96 | 0
p21 | 360 | 9 | 60 | - | **5474.84** | **6177.99** | **6243.98** | 12.84 | 1.06
p22 | 360 | 9 | 60 | 200 | **5702.16** | **6017.39** | **6055.04** | 5.53 | 0.62
p23 | 360 | 9 | 60 | 180 | **6095.46** | **6145.58** | **6160.56** | 0.82 | 0.24


#### Optimization and tradeoff
The choice of stopping after 1200 generations was rather arbitrary, as I felt that it was an OK tradeoff between time spent
and solution quality. Such a tradeoff must typically always be considered when working with GAs.
The algorithm will typically improve quickly at first, and then the curve will slowly flatten out. To illustrate, the figure below
shows the progression of mean best fitness (distance) for 10 independent runs from 0 to 800 generations for the implemented algorithm run on p10.

![mbf-p10](/data/progression/MBF_p10_avg10.png)

## References
[1]  B. Ombuki-Berman and F. T. Hanshar. “Using Genetic Algorithms for Multi-depot
Vehicle Routing.” In: F. B. Pereira and J. Tavares (eds). “Bio-inspired Algorithms for
the Vehicle Routing Problem.” Studies in Computational Intelligence, vol 161.
Springer 2009.

[2]  J. Cordeau, M. Gendreau and G. Laporte. "A tabu search heuristic for periodic and multi-depot vehicle routing problems". 1997.

[3]  Ivars Dzalbs and Tatiana Kalgonava. "Hybrid 2-stage Imperialist Competitive Algorithm with Ant Colony Optimization for Solving Multi-Depot Vehicle Routing Problem". 2020.