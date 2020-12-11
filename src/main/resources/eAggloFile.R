library("ecp");

eDivFromFile <- function(fileName, ncols, buckets, bucketSize) {
  dataPoints <- scan(fileName)
  mat <- matrix(dataPoints, ncol = ncols, byrow = TRUE)
  member <- rep(1:buckets, each = bucketSize)
  changePoints <- e.agglo(X = mat, member = member, alpha = 1)
  changePoints$estimates

}