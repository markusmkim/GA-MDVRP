# Using a genetic algorithm to solve the Multi-Depot Vehicle Routing Problem
The Multi-Depot Vehicle Routing Problem (MDVRP) is a classic example of a NP-Hard combinatorial optimization problem.
Here, a genetic algorithm (GA) is implemented and applied to 23 benchmark problems to find approximate solutions in polynomial time.
The implementation of the GA, in particular the evolutionary operators, is inspired by the work of Ombuki-Berman and Hanshar [1].

The rules of an MDVRP can be formulated as follows:
- A set of geographically dispersed customers with known demands are to be
  serviced by a fleet of vehicles. Each customer is to be fully serviced exactly once.
- Several vehicles are assigned to each depot. A vehicle must start from and return to the same depot.
- Each vehicle has a limited service capacity *Q*. That is, for a given vehicle the total customer demand on its route cannot exceed Q.
- For some problems there is also a maximum duration *R* for each single route.
- The objective is to maximize or minimize some goal. Here, **the objective is to minimize the total distance travelled by all vehicles
  across the depots to serve all customers**
  

## Results
Instance | p01 | p08 | p10
:-: | :-: | :-: | :-:
Solution | ![p01](/data/solutionImages/p01.png) | ![p02](/data/solutionImages/p08.png) | ![p03](/data/solutionImages/p10.png)
**Distance** | **597.66** | **4692.56** | **3973.76**
Depots | 4 | 2 | 4
Customers | 50 | 249 | 249
*Q* | 80 | 500 | 500
*R* | - | 310 | 310


Instance | Customers | Depots | *Q* | *R* | BKB | GA-best | GA-avg | Loss | Deviation
:-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-:
p01 | x | y | z | a | b | c | d | e | f
p02 | x | y | z | a | b | c | d | e | f
p03 | x | y | z | a | b | c | d | e | f
p04 | x | y | z | a | b | c | d | e | f
p05 | x | y | z | a | b | c | d | e | f
p06 | x | y | z | a | b | c | d | e | f
p07 | x | y | z | a | b | c | d | e | f
p08 | x | y | z | a | b | c | d | e | f
p09 | x | y | z | a | b | c | d | e | f
p10 | x | y | z | a | b | c | d | e | f
p11 | x | y | z | a | b | c | d | e | f
p12 | x | y | z | a | b | c | d | e | f
p13 | x | y | z | a | b | c | d | e | f
p14 | x | y | z | a | b | c | d | e | f
p15 | x | y | z | a | b | c | d | e | f
p16 | x | y | z | a | b | c | d | e | f
p17 | x | y | z | a | b | c | d | e | f
p18 | x | y | z | a | b | c | d | e | f
p19 | x | y | z | a | b | c | d | e | f
p20 | x | y | z | a | b | c | d | e | f
p21 | x | y | z | a | b | c | d | e | f
p22 | x | y | z | a | b | c | d | e | f
p23 | x | y | z | a | b | c | d | e | f


## References
[1]  B. Ombuki-Berman and F. T. Hanshar. “Using Genetic Algorithms for Multi-depot
Vehicle Routing.” In: F. B. Pereira and J. Tavares (eds). “Bio-inspired Algorithms for
the Vehicle Routing Problem.” Studies in Computational Intelligence, vol 161.
Springer 2009.