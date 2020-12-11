library("ecp");

function() {
  set.seed(250);
  period1 <- rnorm(100);
  period2 <- rnorm(100, 0, 3);
  period3 <- rnorm(100, 2, 1);
  period4 <- rnorm(100, 2, 4);
  Xnorm <- matrix(c(period1, period2, period3, period4), ncol = 1);
  changePoints <- e.divisive(Xnorm, R = 499, alpha = 2);
  changePoints$estimates;
}