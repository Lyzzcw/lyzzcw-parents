package lyzzcw.work.component.common.random;


public interface IRandomService {
  RandomTuple getRandoms(int paramInt, long paramLong1, long paramLong2);
  
  @Deprecated
  RandomTuple getRandoms(int paramInt1, int paramInt2, int paramInt3);
  
  RandomTuple getRandoms(int paramInt);
}

