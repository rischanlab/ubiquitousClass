

#install.packages('rwt')
library('rwt')
#install.packages('fftw')
library('rwt')


setwd("D:/Dropbox/LAB/COURSE/3/ubi/projects/data/Gde/Walking/B")
file <- list.files()


extracted_feature <- data.frame()
#Import data from Andro sensor
for(file_counter in 1:length(file))
{
  raw_data <- read.csv(file[file_counter],header=TRUE)
  #Extract accelerometer data from Y and Z axis and time scaling
  X_data <- raw_data$ACCELEROMETER.X..m.s...
  Y_data <- raw_data$ACCELEROMETER.Y..m.s...
  Z_data <- raw_data$ACCELEROMETER.Z..m.s...
  M_data <- sqrt( ((X_data)*(X_data))+((Y_data)*(Y_data))+((Z_data)*(Z_data)) )
  
  time_elapsed <- raw_data$Time.since.start.in.ms[length(raw_data$Time.since.start.in.ms)]
  time_scale <- time_elapsed/512
  
  #Linear interpolate data from X,Y, Z and M axis
  fx <- approxfun(1:length(X_data),X_data,method='linear')
  fy <- approxfun(1:length(Y_data),Y_data,method='linear')
  fz <- approxfun(1:length(Z_data),Z_data,method='linear')
  fm <- approxfun(1:length(M_data),M_data,method='linear')
  
  
  X_data2 <- matrix(fx(seq(1,length(X_data),length.out=512)),1,length(fx(seq(1,length(X_data),length.out=512))))
  Y_data2 <- matrix(fy(seq(1,length(Y_data),length.out=512)),1,length(fy(seq(1,length(Y_data),length.out=512))))
  Z_data2 <- matrix(fz(seq(1,length(Z_data),length.out=512)),1,length(fz(seq(1,length(Z_data),length.out=512))))
  M_data2 <- matrix(fm(seq(1,length(M_data),length.out=512)),1,length(fm(seq(1,length(M_data),length.out=512))))
  
  #Apply daubechies filter 6 3 level
  h <- daubcqf(6)
  
  X_data3 <- denoise.dwt(X_data2,h$h.0)
  X_data4 <- denoise.dwt(X_data3$xd,h$h.0)
  X_data5 <- denoise.dwt(X_data4$xd,h$h.0)
  fx5 <- approxfun(1:length(X_data5$xd),X_data5$xd)
  curve(fx,1,length(X_data))
  curve(fx5,1,length(X_data5$xd))
  
  
  Y_data3 <- denoise.dwt(Y_data2,h$h.0)
  Y_data4 <- denoise.dwt(Y_data3$xd,h$h.0)
  Y_data5 <- denoise.dwt(Y_data4$xd,h$h.0)
  fy5 <- approxfun(1:length(Y_data5$xd),Y_data5$xd)
  curve(fy,1,length(Y_data))
  curve(fy5,1,length(Y_data5$xd))
  
  Z_data3 <- denoise.dwt(Z_data2,h$h.0)
  Z_data4 <- denoise.dwt(Z_data3$xd,h$h.0)
  Z_data5 <- denoise.dwt(Z_data4$xd,h$h.0)
  fz5 <- approxfun(1:length(Z_data5$xd),Z_data5$xd)
  curve(fz,1,length(Z_data))
  curve(fz5,1,length(Z_data5$xd))
  
  M_data3 <- denoise.dwt(M_data2,h$h.0)
  M_data4 <- denoise.dwt(M_data3$xd,h$h.0)
  M_data5 <- denoise.dwt(M_data4$xd,h$h.0)
  fm5 <- approxfun(1:length(M_data5$xd),M_data5$xd)
  curve(fm,1,length(M_data))
  curve(fm5,1,length(M_data5$xd))
  
  #Change X, Y, Z and M data to data frame
  X_data6 <- data.frame(1:length(X_data5$xd),matrix(X_data5$xd,length(X_data5$xd),1))
  names(X_data6) <- c('Index','Value')
  Y_data6 <- data.frame(1:length(Y_data5$xd),matrix(Y_data5$xd,length(Y_data5$xd),1))
  names(Y_data6) <- c('Index','Value')
  Z_data6 <- data.frame(1:length(Z_data5$xd),matrix(Z_data5$xd,length(Z_data5$xd),1))
  names(Z_data6) <- c('Index','Value')
  M_data6 <- data.frame(1:length(M_data5$xd),matrix(M_data5$xd,length(M_data5$xd),1))
  names(M_data6) <- c('Index','Value')
  
  #Peak detection only using Z axis
  peak_counter <- 1
  all_peak <- Z_data6[1,]
  for(index in 2:511)
  {
    if(Z_data6$Value[index]>Z_data6$Value[index+1] & Z_data6$Value[index]>Z_data6$Value[index-1])
    {
      all_peak[peak_counter,] <- Z_data6[index,]
      peak_counter <- peak_counter+1
    }
  }
  peak_mean <- mean(all_peak$Value)
  peak_sd <- sd(all_peak$Value)
  true_peak_threshold <- peak_mean-((1/3)*peak_sd)
  
  
  true_peak_counter <- 1
  true_peak <- all_peak[1,]
  for(counter in 1:length(all_peak$Value))
  {
    if(all_peak$Value[counter]>true_peak_threshold)
    {
      true_peak[true_peak_counter,] <- all_peak[counter,]
      true_peak_counter <- true_peak_counter+1 
    }
  }
  true_peak 
  
  #Extract gait cycle between true peaks in X, Y, Z, and M axis
  total_gait_cycle <- length(true_peak$Value)-1
  for (cycle_counter in 1:total_gait_cycle)
  {
    cycle_counter2 <- cycle_counter+1
    temp0 <- paste("gait_cycles_X",cycle_counter,sep = "_")
    temp00 <- X_data6[true_peak$Index[cycle_counter]:true_peak$Index[cycle_counter2],]
    temp1 <- paste("gait_cycles_Y",cycle_counter,sep = "_")
    temp2 <- Y_data6[true_peak$Index[cycle_counter]:true_peak$Index[cycle_counter2],]
    temp3 <- paste("gait_cycles_Z",cycle_counter,sep = "_")
    temp4 <- Z_data6[true_peak$Index[cycle_counter]:true_peak$Index[cycle_counter2],]
    temp5 <- paste("gait_cycles_M",cycle_counter,sep = "_")
    temp6 <- M_data6[true_peak$Index[cycle_counter]:true_peak$Index[cycle_counter2],]
    assign(temp0,temp00)
    assign(temp1,temp2)
    assign(temp3,temp4)
    assign(temp5,temp6)
  }
  
  
  #Feature extraction from each gait cycle
  
  #40 FFT Coefficient from Y-axis and Z-axis
  fft_coef_all <- data.frame()
  for  (counter in 1:total_gait_cycle)
  {
    temp1 <- paste("gait_cycles_Y",counter,sep = "_") 
    temp2 <- (get(temp1)$Index[length(get(temp1)$Index)]-get(temp1)$Index[1])*time_scale
    time_gap <- temp2
    
    temp1 <- paste("gait_cycles_X",counter,sep = "_")
    temp2 <- get(temp1)$Value
    temp_func <- approxfun(1:length(temp2),temp2,method='linear')
    temp3 <- matrix(temp_func(seq(1,length(temp2),length.out=256)),1,length(temp_func(seq(1,length(temp2),length.out=256))))
    fft_plan <- planFFT(length(temp3))
    fft_x <- FFT(temp3,plan=fft_plan)
    re_coef_fft_x <- matrix(Re(fft_x[1:40]),1,length(Re(fft_x[1:40])))
    
    temp1 <- paste("gait_cycles_Y",counter,sep = "_")
    temp2 <- get(temp1)$Value
    temp_func <- approxfun(1:length(temp2),temp2,method='linear')
    temp3 <- matrix(temp_func(seq(1,length(temp2),length.out=256)),1,length(temp_func(seq(1,length(temp2),length.out=256))))
    fft_plan <- planFFT(length(temp3))
    fft_y <- FFT(temp3,plan=fft_plan)
    re_coef_fft_y <- matrix(Re(fft_y[1:40]),1,length(Re(fft_y[1:40])))
    
    
    temp1 <- paste("gait_cycles_Z",counter,sep = "_")
    temp2 <- get(temp1)$Value
    temp_func <- approxfun(1:length(temp2),temp2,method='linear')
    temp3 <- matrix(temp_func(seq(1,length(temp2),length.out=256)),1,length(temp_func(seq(1,length(temp2),length.out=256))))
    fft_plan <- planFFT(length(temp3))
    fft_z <- FFT(temp3,plan=fft_plan)
    re_coef_fft_z <- matrix(Re(fft_z[1:40]),1,length(Re(fft_z[1:40])))
    
    temp1 <- paste("gait_cycles_M",counter,sep = "_")
    temp2 <- get(temp1)$Value
    temp_func <- approxfun(1:length(temp2),temp2,method='linear')
    temp3 <- matrix(temp_func(seq(1,length(temp2),length.out=256)),1,length(temp_func(seq(1,length(temp2),length.out=256))))
    fft_plan <- planFFT(length(temp3))
    fft_m <- FFT(temp3,plan=fft_plan)
    re_coef_fft_m <- matrix(Re(fft_m[1:40]),1,length(Re(fft_m[1:40])))
    
    temp_coef <- cbind(time_gap,re_coef_fft_x,re_coef_fft_y,re_coef_fft_z,re_coef_fft_m)
    temp_coef <- as.data.frame(temp_coef)
    fft_coef_all <- rbind(fft_coef_all,temp_coef)
  }
  
  extracted_feature <- rbind(extracted_feature,fft_coef_all)
}



#Write data to file



write.csv(extracted_feature,sprintf("D:/Dropbox/LAB/COURSE/3/ubi/projects/data/Gde/Walking/fftB/gde_FFT.csv"))